package com.soongsil.soongpal.board.repository;

import com.soongsil.soongpal.board.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    int countByBoardId (Long boardId);
}
