package com.soongsil.soongpal.board.controller;

import com.soongsil.soongpal.board.dto.BoardCreateReqDto;
import com.soongsil.soongpal.board.dto.BoardResDto;
import com.soongsil.soongpal.board.service.BoardService;
import com.soongsil.soongpal.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/board-purchases")
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<CommonResDto<BoardResDto>> createBoard(@Valid @RequestBody BoardCreateReqDto BoardCreateReqDto) {
        BoardResDto dto = boardService.createBoard(BoardCreateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 생성", dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<List<BoardResDto>>> getAllBoards() {
        List<BoardResDto> dto = boardService.getAllBoards();
        return new ResponseEntity<>(new CommonResDto<>("모든 공동구매 게시글 조회", dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto<BoardResDto>> getBoardById(@PathVariable Long id) {
        BoardResDto dto = boardService.getBoardById(id);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 상세 조회", dto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResDto<BoardResDto>> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardCreateReqDto BoardCreateReqDto) {
        BoardResDto dto = boardService.updateBoard(id, BoardCreateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 수정", dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResDto<BoardResDto>> deleteBoard(@PathVariable Long id) {
        BoardResDto dto = boardService.deleteBoard(id);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 삭제", dto), HttpStatus.OK);
    }
}