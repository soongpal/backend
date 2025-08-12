package com.soongsil.soongpal.chat.service;

import com.soongsil.soongpal.chat.dto.ChatMessageResDto;
import com.soongsil.soongpal.chat.dto.ChatPageResDto;
import com.soongsil.soongpal.chat.repository.ChatMessageRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatPageResDto<ChatMessageResDto> getMessages(Long roomId, Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("createdAt").descending());
        boolean hasAccess = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId).isPresent();
        if (!hasAccess) {
            throw new IllegalArgumentException("접근 권한이 없는 채팅방입니다.");
        }

        Page<ChatMessageResDto> messages = chatMessageRepository.findByChatRoomId(roomId, pageable)
                .map(ChatMessageResDto::from);
        return  ChatPageResDto.from(messages);
    }


}
