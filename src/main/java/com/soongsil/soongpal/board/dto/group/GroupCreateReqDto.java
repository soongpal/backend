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

    public static GroupBoard toEntity(GroupCreateReqDto groupCreateReqDto) {
        return GroupBoard.builder()
                .title(groupCreateReqDto.getTitle())
                .content(groupCreateReqDto.getContent())
                .url(groupCreateReqDto.getUrl())
                .location(groupCreateReqDto.getLocation())
                .build();
    }
}
