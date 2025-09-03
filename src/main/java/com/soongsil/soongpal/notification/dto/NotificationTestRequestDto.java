package com.soongsil.soongpal.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 관리자 테스트용
 */
@Getter
@NoArgsConstructor
public class NotificationTestRequestDto {
    private String fcmToken;
    private String title;
    private String body;
}