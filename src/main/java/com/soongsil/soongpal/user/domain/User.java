package com.soongsil.soongpal.user.domain;

import com.soongsil.soongpal.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kakaoId;
    private String nickName;
    private String email;
    private String refreshToken;

    @Builder
    public User(String kakaoId, String nickName, String email) {
        this.kakaoId = kakaoId;
        this.nickName = nickName;
        this.email = email;
    }

    public void updateNickname(String nickName) {
        this.nickName = nickName;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}