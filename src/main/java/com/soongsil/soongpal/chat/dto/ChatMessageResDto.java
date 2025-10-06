package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResDto {

    private Long roomId;
    private Long senderId;
    private String senderName;
    private String content;
    private Integer unreadCount;
    private LocalDateTime createdAt;

    public static ChatMessageResDto from(ChatMessage message, Integer unreadCount) {
        String senderName = message.getSender().getDeletedAt() != null ?
            "탈퇴한 회원" : message.getSender().getNickName();

        return ChatMessageResDto.builder()
                .roomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(senderName)
                .content(message.getContent())
                .unreadCount(unreadCount)
                .createdAt(message.getCreatedAt())
                .build();
    }

}