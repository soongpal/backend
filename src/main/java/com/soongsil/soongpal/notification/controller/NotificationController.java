package com.soongsil.soongpal.notification.controller;

import com.soongsil.soongpal.notification.dto.NotificationTestRequestDto;
import com.soongsil.soongpal.notification.service.FCMNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * 관리자 테스트용
 */
@Tag(name = "알림", description = "FCM 알림 테스트 관련 API")
@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final FCMNotificationService fcmNotificationService;

    @Operation(summary = "FCM 알림 테스트", description = "지정된 FCM 토큰으로 테스트 알림을 전송합니다.")
    @PostMapping("/test")
    public ResponseEntity<String> sendTestNotification(@RequestBody NotificationTestRequestDto request) {
        fcmNotificationService.sendNotificationToUser(
            request.getFcmToken(),
            request.getTitle(),
            request.getBody()
        );
        return ResponseEntity.ok("알림 전송 완료");
    }
}