package com.soongsil.soongpal.board.repository;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByCategory(BoardCategory category, Pageable pageable);
    Page<Board> findByStatus(BoardStatus status, Pageable pageable);
    Page<Board> findByCategoryAndStatus(BoardCategory category, BoardStatus status, Pageable pageable);
    Page<Board> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
