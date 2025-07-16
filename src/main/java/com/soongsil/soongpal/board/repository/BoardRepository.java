package com.soongsil.soongpal.board.repository;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByCategory(BoardCategory category);
    List<Board> findByStatus(BoardStatus status);
    List<Board> findByCategoryAndStatus(BoardCategory category, BoardStatus status);

    List<Board> findByTitleContainingIgnoreCase(String keyword);
}
