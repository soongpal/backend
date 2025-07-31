package com.soongsil.soongpal.chat.repository;

import com.soongsil.soongpal.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
