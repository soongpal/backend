package com.soongsil.soongpal.user.service;
import com.soongsil.soongpal.board.repository.LikeRepository;
import com.soongsil.soongpal.board.service.BoardService;
import com.soongsil.soongpal.user.service.jwt.JwtTokenProvider;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.dto.*;
import com.soongsil.soongpal.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BoardService boardService;
    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient = WebClient.create();

    @Value("${kakao-admin.key}")
    private String kakaoAdminKey;

    @Transactional
    public TokenPair registerNewUser(String tempToken, String nickname) {
        if (!jwtTokenProvider.validateToken(tempToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        OAuthAttributes attributes = jwtTokenProvider.getAttributesFromTempToken(tempToken);

        if (userRepository.findByNickName(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User newUser = attributes.toEntity(nickname);
        userRepository.save(newUser);

        String userId = String.valueOf(newUser.getId());
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        newUser.updateRefreshToken(refreshToken);

        return new TokenPair(accessToken, refreshToken);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));

        likeRepository.deleteAllByUser(user);
        boardService.deleteAllBoardsByUser(user);
        unlinkKakaoAccount(user.getKakaoId());
        userRepository.delete(user);
    }

    private void unlinkKakaoAccount(String kakaoId) {
        String unlinkUri = "https://kapi.kakao.com/v1/user/unlink";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", kakaoId);

        webClient.post()
            .uri(unlinkUri)
            .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoAdminKey)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));
        return new UserInfoResponseDto(user);
    }

    @Transactional
    public UserInfoResponseDto updateUserInfo(Long userId, UserUpdateRequestDto updateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));

        String newNickname = updateRequestDto.getNickname();
        if (userRepository.findByNickName(newNickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        user.updateNickname(newNickname);

        return new UserInfoResponseDto(user);
    }

    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));
        user.updateRefreshToken(refreshToken);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));

        user.updateRefreshToken(null);
    }

    public TokenRefreshResponseDto reissueTokens(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        Long userId = Long.parseLong(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("DB의 리프레시 토큰과 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(userId));

        return new TokenRefreshResponseDto(newAccessToken);
    }
}