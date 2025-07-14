package com.soongsil.soongpal.board.service;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.dto.BoardCreateReqDto;
import com.soongsil.soongpal.board.dto.BoardResDto;
import com.soongsil.soongpal.board.repository.BoardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResDto createBoard(BoardCreateReqDto boardCreateReqDto) {
        Board board = BoardCreateReqDto.toEntity(boardCreateReqDto);
        boardRepository.save(board);
        return BoardResDto.from(board);
    }

    public List<BoardResDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(BoardResDto::from)
                .toList();
    }

    public BoardResDto getBoardById(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return BoardResDto.from(findBoard);
    }

    public BoardResDto updateBoard(Long id, @Valid BoardCreateReqDto boardCreateReqDto) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        findBoard.update(BoardCreateReqDto.toEntity(boardCreateReqDto));
        return BoardResDto.from(findBoard);
    }

    public BoardResDto deleteBoard(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boardRepository.deleteById(id);
        return BoardResDto.from(findBoard);
    }
}