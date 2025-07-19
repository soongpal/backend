package com.soongsil.soongpal.board.service;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import com.soongsil.soongpal.board.domain.Like;
import com.soongsil.soongpal.board.dto.BoardCreateReqDto;
import com.soongsil.soongpal.board.dto.BoardResDto;
import com.soongsil.soongpal.board.dto.BoardUpdateReqDto;
import com.soongsil.soongpal.board.dto.LikeResDto;
import com.soongsil.soongpal.board.repository.BoardRepository;
import com.soongsil.soongpal.board.repository.LikeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    public BoardResDto createBoard(BoardCreateReqDto boardCreateReqDto) {
        Board board = BoardCreateReqDto.toEntity(boardCreateReqDto);
        boardRepository.save(board);
        return BoardResDto.from(board);
    }

    public BoardResDto getBoardById(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return BoardResDto.from(findBoard);
    }

    @Transactional
    public BoardResDto updateBoard(Long id, @Valid BoardUpdateReqDto boardUpdateReqDto) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        findBoard.update(boardUpdateReqDto.toEntity());
        return BoardResDto.from(findBoard);
    }

    public BoardResDto deleteBoard(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boardRepository.deleteById(id);
        return BoardResDto.from(findBoard);
    }

    public List<BoardResDto> getFilteredBoards(String keyword, BoardCategory category, BoardStatus status) {
        if (keyword != null && !keyword.isEmpty()) {
            // 키워드로 게시글 제목 검색
            return boardRepository.findByTitleContainingIgnoreCase(keyword).stream()
                    .map(BoardResDto::from)
                    .collect(Collectors.toList());
        }
        else if (category != null && status != null) {
            // 카테고리 + 상태 조합 검색
            return boardRepository.findByCategoryAndStatus(category, status).stream()
                    .map(BoardResDto::from)
                    .collect(Collectors.toList());
        } else if (category != null) {
            // 게시글 카테고리 검색
            return boardRepository.findByCategory(category).stream()
                    .map(BoardResDto::from)
                    .collect(Collectors.toList());
        } else if (status != null) {
            // 현재 게시글 상태 검색
            return boardRepository.findByStatus(status).stream()
                    .map(BoardResDto::from)
                    .collect(Collectors.toList());
        } else {
            // 모든 게시글 조회
            return boardRepository.findAll().stream()
                    .map(BoardResDto::from)
                    .collect(Collectors.toList());
        }
    }

    public LikeResDto addLike(Long boardId) {
        Like like = Like.builder()
                .boardId(boardId)
                .build();
        likeRepository.save(like);

        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }
}