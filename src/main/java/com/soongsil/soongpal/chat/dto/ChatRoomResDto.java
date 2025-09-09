package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResDto {

    private Long id;
    private String name;
    private String productTitle;
    private ChatRoomType type;
    private int userCount;
    private List<ChatRoomUserResDto> users;
    private ChatMessageResDto lastMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatRoomResDto of(ChatRoom chatRoom, String name, String productTitle, List<ChatRoomUserResDto> users, ChatMessageResDto lastMessage) {
        return ChatRoomResDto.builder()
                .id(chatRoom.getId())
                .name(name)
                .productTitle(productTitle)
                .type(chatRoom.getType())
                .userCount(chatRoom.getChatRoomUsers().size())
                .users(users)
                .lastMessage(lastMessage)
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }

}