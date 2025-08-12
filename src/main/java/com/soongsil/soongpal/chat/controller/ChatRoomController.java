package com.soongsil.soongpal.chat.controller;

import com.soongsil.soongpal.chat.dto.ChatRoomCreateReqDto;
import com.soongsil.soongpal.chat.dto.ChatRoomResDto;
import com.soongsil.soongpal.chat.service.ChatRoomService;
import com.soongsil.soongpal.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @PostMapping
    public ResponseEntity<CommonResDto<ChatRoomResDto>> createChatRoom(
            @RequestBody ChatRoomCreateReqDto dto
    ) {
        ChatRoomResDto chatRoom = chatRoomService.createChatRoom(dto);
        return new ResponseEntity<>(new CommonResDto<>("채팅방이 생성되었습니다.", chatRoom), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<CommonResDto<ChatRoomResDto>> getChatRoom(
            @PathVariable Long roomId
    ) {
        Long userId = getUserId();
        ChatRoomResDto chatRoom = chatRoomService.getChatRoom(roomId, userId);
        return new ResponseEntity<>(new CommonResDto<>("채팅방 정보를 조회했습니다.", chatRoom), HttpStatus.OK);
    }

    private Long getUserId() {
        return 2L;
    }

}