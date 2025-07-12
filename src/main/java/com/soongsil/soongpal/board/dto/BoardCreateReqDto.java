package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class BoardCreateReqDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String url;
    private String location;

    @NotNull
    private BoardStatus status;

    public static Board toEntity(BoardCreateReqDto boardCreateReqDto) {
        return Board.builder()
                .title(boardCreateReqDto.getTitle())
                .content(boardCreateReqDto.getContent())
                .url(boardCreateReqDto.getUrl())
                .location(boardCreateReqDto.getLocation())
                .status(boardCreateReqDto.getStatus())
                .build();
    }
}
