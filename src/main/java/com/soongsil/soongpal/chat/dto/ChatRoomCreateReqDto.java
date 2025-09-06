package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "채팅방 타입", example = "GROUP")
    @NotNull
    private ChatRoomType type;

    public static ChatRoom toEntity(ChatRoomCreateReqDto dto) {
        return ChatRoom.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }
}