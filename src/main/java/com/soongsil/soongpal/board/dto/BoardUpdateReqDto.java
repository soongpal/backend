package com.soongsil.soongpal.board.dto;

import com.soongsil.soongpal.board.domain.BoardStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateReqDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String url;
    private String location;

    @NotNull
    private BoardStatus status;
}
