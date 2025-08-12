package com.soongsil.soongpal.chat.controller;

import com.soongsil.soongpal.chat.dto.ChatMessageResDto;
import com.soongsil.soongpal.chat.dto.ChatPageResDto;
import com.soongsil.soongpal.chat.service.ChatMessageService;
import com.soongsil.soongpal.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<CommonResDto<ChatPageResDto<ChatMessageResDto>>> getMessages(
            @RequestParam Long roomId,
            @RequestParam(defaultValue = "0") int page
    ) {
        Long userId = getUserId();
        ChatPageResDto<ChatMessageResDto> messages = chatMessageService.getMessages(roomId, userId, page);
        return new ResponseEntity<>(new CommonResDto<>("메시지 목록을 조회했습니다.", messages), HttpStatus.OK);
    }

    private Long getUserId() {
        return 2L;
    }

}