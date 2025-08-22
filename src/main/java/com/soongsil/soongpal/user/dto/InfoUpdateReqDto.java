package com.soongsil.soongpal.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;


@Getter
public class InfoUpdateReqDto {

    @NotEmpty
    @Schema(description = "사용자의 닉네임", example = "슝슝이")
    private String nickName;

}
