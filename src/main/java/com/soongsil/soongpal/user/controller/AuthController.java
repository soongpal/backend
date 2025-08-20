package com.soongsil.soongpal.user.controller;

import com.soongsil.soongpal.user.dto.AuthResponseDto;
import com.soongsil.soongpal.user.dto.NicknameRequestDto;
import com.soongsil.soongpal.user.dto.TokenRefreshRequestDto;
import com.soongsil.soongpal.user.dto.TokenRefreshResponseDto;
import com.soongsil.soongpal.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "최종 회원가입", description = "임시 토큰과 닉네임을 받아 최종 회원가입을 처리합니다.")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @RequestHeader("Authorization") String tempToken,
            @RequestBody NicknameRequestDto nicknameRequestDto) {

        String token = tempToken.substring(7);
        AuthResponseDto authResponseDto = authService.registerNewUser(token, nicknameRequestDto.getNickname());

        return ResponseEntity.ok(authResponseDto);
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshTokens(@RequestBody TokenRefreshRequestDto requestDto) {
        TokenRefreshResponseDto responseDto = authService.reissueTokens(requestDto.getRefreshToken());
        return ResponseEntity.ok(responseDto);
    }
}