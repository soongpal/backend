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
