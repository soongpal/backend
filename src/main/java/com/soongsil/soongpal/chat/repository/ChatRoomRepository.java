package com.soongsil.soongpal.chat.repository;

import com.soongsil.soongpal.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatRoomUsers cru WHERE cr.id = :roomId AND cru.user.id = :userId")
    Optional<ChatRoom> findChatRoomByIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatRoomUsers cru WHERE cru.user.id = :userId ORDER BY cr.updatedAt DESC")
    List<ChatRoom> findChatRoomsByUserId(@Param("userId") Long userId);
    
    Optional<ChatRoom> findByBoardId(Long boardId);

}
