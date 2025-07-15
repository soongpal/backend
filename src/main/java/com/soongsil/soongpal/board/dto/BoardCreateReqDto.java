package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class BoardCreateReqDto {

    @Schema(description = "게시글의 제목", example = "콜라 30개 묶음 공동구매")
    @NotBlank
    private String title;

    @Schema(description = "게시글의 내용", example = "전 10개만 필요해요!")
    @NotBlank
    private String content;

    @Schema(description = "상품 관련 URL (선택 사항)", example = "https://example.com/cola")
    private String url;
    @Schema(description = "모임 장소 또는 거래 위치 (선택 사항)", example = "기숙사 1층 로비")
    private String location;

    @Schema(description = "게시글 상태", example = "GROUP", allowableValues = {"GROUP", "USED"})
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
