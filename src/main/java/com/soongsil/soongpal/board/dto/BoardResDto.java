package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResDto {
    private Long id;
    private String title;
    private String content;
    private Integer price;
    private String url;
    private String location;
    private BoardCategory category;
    private BoardStatus status;

    public static BoardResDto from(Board board) {
        return BoardResDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .price(board.getPrice())
                .url(board.getUrl())
                .location(board.getLocation())
                .category(board.getCategory())
                .status(board.getStatus())
                .build();
    }
}