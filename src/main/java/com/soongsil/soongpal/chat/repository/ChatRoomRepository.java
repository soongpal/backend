package com.soongsil.soongpal.chat.repository;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
