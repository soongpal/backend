package com.soongsil.soongpal.chat.dto;

import com.soongsil.soongpal.chat.domain.ChatRoomUser;
import com.soongsil.soongpal.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomUserResDto {

    private Long userId;
    private String userName;
    private String profileImage;

    public static ChatRoomUserResDto from(ChatRoomUser roomUser) {
        User u = roomUser.getUser();
        return ChatRoomUserResDto.builder()
                .userId(u.getId())
                .userName(u.getNickName())
                .build();
    }

}