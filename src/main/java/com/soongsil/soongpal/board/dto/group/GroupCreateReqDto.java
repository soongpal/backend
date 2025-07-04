package com.soongsil.soongpal.board.dto.group;

import com.soongsil.soongpal.board.domain.GroupBoard;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class GroupCreateReqDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String url;
    private String location;

    public static GroupBoard toEntity(GroupCreateReqDto dto) {
        return GroupBoard.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .url(dto.getUrl())
                .location(dto.getLocation())
                .build();
    }
}
