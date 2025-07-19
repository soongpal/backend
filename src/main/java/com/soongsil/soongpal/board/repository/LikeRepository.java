package com.soongsil.soongpal.board.repository;


import com.soongsil.soongpal.board.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    int countByBoardId (Long boardId);

    Optional<Like> findByBoardId(Long boardId);
}
