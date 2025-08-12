package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatMessage;
import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReqDto {

    @NotBlank
    private Long roomId;

    @NotBlank
    private Long senderId;

    @NotBlank
    private String content;

    public static ChatMessage toEntity(ChatMessageReqDto dto, User sender, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(dto.getContent())
                .build();
    }

}