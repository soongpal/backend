package com.soongsil.soongpal.board.repository;

import com.soongsil.soongpal.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {



}
