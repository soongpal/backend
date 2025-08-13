package com.soongsil.soongpal.chat.service;

import com.soongsil.soongpal.chat.domain.ChatMessage;
import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.dto.ChatMessageReqDto;
import com.soongsil.soongpal.chat.dto.ChatMessageResDto;
import com.soongsil.soongpal.chat.repository.ChatMessageRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomRepository;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;


    public ChatMessageResDto saveMessage(Long roomId, ChatMessageReqDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 없습니다."));

        User findUser = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        ChatMessage chatMessage = ChatMessageReqDto.toEntity(dto, findUser, chatRoom);
        
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageResDto.from(savedMessage);
    }
}

