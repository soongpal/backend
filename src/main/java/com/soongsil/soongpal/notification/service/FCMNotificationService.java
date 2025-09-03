package com.soongsil.soongpal.notification.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendNotificationToUser(String fcmToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(fcmToken)
                    .build();

            String response = firebaseMessaging.send(message);
            log.info("FCM 알림 전송 성공: {}", response);
        } catch (Exception e) {
            log.error("FCM 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    public void sendChatNotification(String fcmToken, String senderName, String message, Long roomId) {
        try {
            Message fcmMessage = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(senderName + "님의 메시지")
                            .setBody(message)
                            .build())
                    .putData("type", "chat")
                    .putData("roomId", roomId.toString())
                    .putData("senderName", senderName)
                    .setToken(fcmToken)
                    .build();

            String response = firebaseMessaging.send(fcmMessage);
            log.info("채팅 FCM 알림 전송 성공: {}", response);
        } catch (Exception e) {
            log.error("채팅 FCM 알림 전송 실패: {}", e.getMessage(), e);
        }
    }
}