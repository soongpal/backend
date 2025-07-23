package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardImage;
import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> imageUrls;

    public static BoardResDto from(Board board) {
        List<String> imageUrls = board.getBoardImages().stream()
                .map(BoardImage::getImageUrl)
                .collect(Collectors.toList());

        return BoardResDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .price(board.getPrice())
                .url(board.getUrl())
                .location(board.getLocation())
                .category(board.getCategory())
                .status(board.getStatus())
                .imageUrls(imageUrls)
                .build();
    }
}