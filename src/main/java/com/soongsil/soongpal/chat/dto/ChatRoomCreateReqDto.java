package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateReqDto {

    private String name;
    @NotBlank
    private ChatRoomType type;
    private List<Long> userIds;

    public static ChatRoom toEntity(ChatRoomCreateReqDto dto) {
        return ChatRoom.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }
}