package com.soongsil.soongpal.board.dto.group;

import com.soongsil.soongpal.board.domain.GroupBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupResDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private String location;

    public static GroupResDto from(GroupBoard groupBoard) {
        return GroupResDto.builder()
                .id(groupBoard.getId())
                .title(groupBoard.getTitle())
                .content(groupBoard.getContent())
                .url(groupBoard.getUrl())
                .location(groupBoard.getLocation())
                .build();
    }
}