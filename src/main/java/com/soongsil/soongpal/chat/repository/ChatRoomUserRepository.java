package com.soongsil.soongpal.chat.repository;

import com.soongsil.soongpal.chat.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Optional<ChatRoomUser> findByChatRoomIdAndUserId(Long roomId, Long userId);

}