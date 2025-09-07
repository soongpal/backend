package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Schema(description = "채팅방 생성 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateReqDto {

    @Schema(description = "채팅방 이름", example = "콜라 공구 채팅방")
    @NotBlank
    private String name;

    public static ChatRoom toEntity(String name, ChatRoomType type) {
        return ChatRoom.builder()
                .name(name)
                .type(type)
                .build();
    }
}