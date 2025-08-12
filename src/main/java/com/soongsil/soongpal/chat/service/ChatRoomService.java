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
import com.soongsil.soongpal.common.exception.ChatErrorCode;
import com.soongsil.soongpal.common.exception.ChatException;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.repository.UserRepository;
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
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_ACCESS_DENIED));
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
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.USER_NOT_FOUND));

        boolean alreadyJoined = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId).isPresent();
        if (alreadyJoined) {
            throw new ChatException(ChatErrorCode.CHAT_ROOM_ALREADY_JOINED);
        }

        ChatRoomUser roomUser = ChatRoomUser.builder()
                .chatRoom(findChatRoom)
                .user(findUser)
                .build();
        chatRoomUserRepository.save(roomUser);
    }

    public void leaveChatRoom(Long roomId, Long userId) {
        ChatRoomUser roomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_JOINED));
        chatRoomUserRepository.delete(roomUser);
    }

    public void deleteChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_DELETE_DENIED));
        chatRoomRepository.delete(chatRoom);
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
