package com.soongsil.soongpal.chat.service;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomUser;
import com.soongsil.soongpal.chat.dto.ChatMessageResDto;
import com.soongsil.soongpal.chat.dto.ChatRoomCreateReqDto;
import com.soongsil.soongpal.chat.dto.ChatRoomResDto;
import com.soongsil.soongpal.chat.dto.ChatRoomUserResDto;
import com.soongsil.soongpal.chat.repository.ChatMessageRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomUserRepository;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;


    public ChatRoomResDto createChatRoom(ChatRoomCreateReqDto dto) {
        ChatRoom savedRoom = chatRoomRepository.save(ChatRoomCreateReqDto.toEntity(dto));

        List<User> users = userRepository.findAllById(dto.getUserIds());
        for (User user : users) {
            ChatRoomUser roomUser = ChatRoomUser.builder()
                    .chatRoom(savedRoom)
                    .user(user)
                    .build();
            chatRoomUserRepository.save(roomUser);
            savedRoom.addUser(roomUser);
        }

        return convertToChatRoomResDto(savedRoom);
    }

    public ChatRoomResDto getChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("접근 권한이 없거나 존재하지 않는 채팅방입니다."));
        return convertToChatRoomResDto(chatRoom);
    }

    public List<ChatRoomResDto> getChatRoomsByUser(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserId(userId);
        return chatRooms.stream()
                .map(this::convertToChatRoomResDto)
                .toList();
    }

    public void joinChatRoom(Long roomId, Long userId) {
        ChatRoom findChatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 없습니다."));

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        boolean alreadyJoined = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId).isPresent();
        if (alreadyJoined) {
            throw new IllegalArgumentException("이미 참가한 채팅방입니다.");
        }

        ChatRoomUser roomUser = ChatRoomUser.builder()
                .chatRoom(findChatRoom)
                .user(findUser)
                .build();
        chatRoomUserRepository.save(roomUser);
    }

    public void leaveChatRoom(Long roomId, Long userId) {
        ChatRoomUser roomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("참가하지 않은 채팅방입니다."));
        chatRoomUserRepository.delete(roomUser);
    }

    private ChatRoomResDto convertToChatRoomResDto(ChatRoom chatRoom) {
        List<ChatRoomUserResDto> users = chatRoom.getChatRoomUsers().stream()
                .map(ChatRoomUserResDto::from)
                .toList();

        ChatMessageResDto lastMessage = chatMessageRepository.findLastMessageByRoomId(chatRoom.getId())
                .map(ChatMessageResDto::from)
                .orElse(null);

        return ChatRoomResDto.of(chatRoom, users, lastMessage);
    }

}
