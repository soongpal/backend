package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.Board;
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

    public static BoardResDto from(Board board) {
        return BoardResDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .url(board.getUrl())
                .location(board.getLocation())
                .build();
    }
}