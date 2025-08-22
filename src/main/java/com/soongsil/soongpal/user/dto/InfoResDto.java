package com.soongsil.soongpal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class InfoResDto {

    private String nickName;

    public static InfoResDto from(String nickName) {
        return InfoResDto.builder()
                .nickName(nickName)
                .build();
    }
}
