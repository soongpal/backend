package com.soongsil.soongpal.chat.service;

import com.soongsil.soongpal.chat.domain.ChatMessage;
import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomUser;
import com.soongsil.soongpal.chat.dto.ChatMessageReqDto;
import com.soongsil.soongpal.chat.dto.ChatMessageResDto;
import com.soongsil.soongpal.chat.repository.ChatMessageRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomUserRepository;
import com.soongsil.soongpal.common.exception.ChatException;
import com.soongsil.soongpal.notification.service.FCMNotificationService;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soongsil.soongpal.common.exception.ChatErrorCode.*;


@Transactional
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final FCMNotificationService fcmNotificationService;


    public ChatMessageResDto saveMessage(Long roomId, ChatMessageReqDto dto, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(CHAT_ROOM_NOT_FOUND));

        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(USER_NOT_FOUND));

        chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoom.getId(), userId)
                .orElseThrow(() -> new ChatException(CHAT_ROOM_ACCESS_DENIED));

        ChatMessage chatMessage = ChatMessageReqDto.toEntity(dto, sender, chatRoom);
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        sendNotificationToOtherUsers(roomId, userId, sender.getNickName(), dto.getContent());

        return ChatMessageResDto.from(savedMessage);
    }

    private void sendNotificationToOtherUsers(Long roomId, Long senderId, String senderName, String message) {
        List<ChatRoomUser> otherUsers = chatRoomUserRepository.findByChatRoomIdAndUserIdNot(roomId, senderId);

        for (ChatRoomUser chatRoomUser : otherUsers) {
            User user = chatRoomUser.getUser();
            if (user.getFcmToken() != null && !user.getFcmToken().isEmpty() && user.getDeletedAt() == null) {
                fcmNotificationService.sendChatNotification(
                    user.getFcmToken(),
                    senderName,
                    message,
                    roomId
                );
            }
        }
    }
}
