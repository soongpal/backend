package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.GroupBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private String location;

    public static BoardResDto from(GroupBoard groupBoard) {
        return BoardResDto.builder()
                .id(groupBoard.getId())
                .title(groupBoard.getTitle())
                .content(groupBoard.getContent())
                .url(groupBoard.getUrl())
                .location(groupBoard.getLocation())
                .build();
    }
}