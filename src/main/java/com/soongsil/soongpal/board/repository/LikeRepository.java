package com.soongsil.soongpal.board.repository;


import com.soongsil.soongpal.board.domain.Like;
import com.soongsil.soongpal.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    int countByBoardId (Long boardId);

    Optional<Like> findByBoardIdAndUserId(Long boardId, Long userId);

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    void deleteAllByUser(User user);
}
