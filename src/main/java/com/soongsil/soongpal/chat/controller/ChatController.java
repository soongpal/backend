package com.soongsil.soongpal.chat.controller;

import com.soongsil.soongpal.chat.dto.ChatMessageDto;
import com.soongsil.soongpal.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long roomId, ChatMessageDto dto) {
        log.info("메시지 도착 = {}", dto.getContent());
        return chatService.saveMessage(roomId, dto);
    }

}
