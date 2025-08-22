package com.soongsil.soongpal.user.handler;

import com.soongsil.soongpal.user.service.jwt.JwtTokenProvider;
import com.soongsil.soongpal.user.dto.PrincipalDetails;
import com.soongsil.soongpal.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String authorizedRedirectUri;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if ("ROLE_GUEST".equals(principalDetails.getAuthorities().iterator().next().getAuthority())) {
            String tempToken = jwtTokenProvider.createTempSignupToken(principalDetails.getOauthAttributes());

            String redirectUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUri + "/signup")
                    .queryParam("temp_token", tempToken)
                    .build().toUriString();
            response.sendRedirect(redirectUrl);

        } else {
            String userId = String.valueOf(principalDetails.getUser().getId());
            String accessToken = jwtTokenProvider.createAccessToken(userId);
            String refreshToken = jwtTokenProvider.createRefreshToken(userId);

            authService.updateRefreshToken(Long.parseLong(userId), refreshToken);

            String redirectUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUri)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", refreshToken)
                    .build().toUriString();
            response.sendRedirect(redirectUrl);
        }
    }
}