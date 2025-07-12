package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.GroupBoard;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class BoardCreateReqDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String url;
    private String location;

    public static GroupBoard toEntity(BoardCreateReqDto boardCreateReqDto) {
        return GroupBoard.builder()
                .title(boardCreateReqDto.getTitle())
                .content(boardCreateReqDto.getContent())
                .url(boardCreateReqDto.getUrl())
                .location(boardCreateReqDto.getLocation())
                .build();
    }
}
