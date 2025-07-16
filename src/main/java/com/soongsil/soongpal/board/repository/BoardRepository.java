package com.soongsil.soongpal.board.repository;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByCategory(BoardCategory category);

    List<Board> findByTitleContainingIgnoreCase(String keyword);
}
