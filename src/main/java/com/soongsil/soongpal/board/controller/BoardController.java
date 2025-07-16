package com.soongsil.soongpal.board.controller;

import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import com.soongsil.soongpal.board.dto.BoardCreateReqDto;
import com.soongsil.soongpal.board.dto.BoardResDto;
import com.soongsil.soongpal.board.dto.BoardUpdateReqDto;
import com.soongsil.soongpal.board.service.BoardService;
import com.soongsil.soongpal.common.dto.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
@Tag(name = "Board Controller", description = "게시글 관련 CRUD 로직을 수행하는 Controller입니다")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @ApiResponse(responseCode = "201", description = "게시글 생성 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 게시글", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Operation(method = "POST", summary = "새 게시글 생성", description = "사용자가 게시글 생성하기 위한 API")
    public ResponseEntity<CommonResDto<BoardResDto>> createBoard(@Valid @RequestBody BoardCreateReqDto boardCreateReqDto) {
        BoardResDto dto = boardService.createBoard(boardCreateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("게시글 생성", dto), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "모든 게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Operation(method = "GET", summary = "모든 게시글 조회", description = "모든 게시글을 조회하기 위한 API")
    public ResponseEntity<CommonResDto<List<BoardResDto>>> getAllBoards() {
        List<BoardResDto> dto = boardService.getAllBoards();
        return new ResponseEntity<>(new CommonResDto<>("모든 게시글 조회", dto), HttpStatus.OK);
    }

    @GetMapping("/category")
    @ApiResponse(responseCode = "200", description = "카테고리 별 게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "400", description = "유효하지 않은 카테고리 값", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Parameter(name = "category", description = "조회할 게시글의 카테고리 상태 (GROUP 또는 USED)", required = true)
    @Operation(method = "GET", summary = "카테고리 별 게시글 조회", description = "category[GROUP,USED]별 게시글 조회하기 위한 API")
    public ResponseEntity<CommonResDto<List<BoardResDto>>> getBoardsByCategory(@RequestParam BoardCategory category) {
        List<BoardResDto> dto = boardService.getBoardsByCategory(category); // 서비스 메서드 호출
        return new ResponseEntity<>(new CommonResDto<>("상태별 게시글 조회", dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Parameter(name = "id", description = "조회할 게시글의 ID", required = true)
    @Operation(method = "GET", summary = "게시글 상세 조회", description = "id를 통해 특정 게시글을 조회하기 위한 API")
    public ResponseEntity<CommonResDto<BoardResDto>> getBoardById(@PathVariable Long id) {
        BoardResDto dto = boardService.getBoardById(id);
        return new ResponseEntity<>(new CommonResDto<>("게시글 상세 조회", dto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Parameter(name = "id", description = "조회할 게시글의 ID", required = true)
    @Operation(method = "PUT", summary = "게시글 수정", description = "게시글의 데이터를 수정하기 위한 API")
    public ResponseEntity<CommonResDto<BoardResDto>> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        BoardResDto dto = boardService.updateBoard(id, boardUpdateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("게시글 수정", dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Parameter(name = "id", description = "조회할 게시글의 ID", required = true)
    @Operation(method = "DELETE", summary = "게시글 삭제", description = "게시글을 삭제하기 위한 API")
    public ResponseEntity<CommonResDto<BoardResDto>> deleteBoard(@PathVariable Long id) {
        BoardResDto dto = boardService.deleteBoard(id);
        return new ResponseEntity<>(new CommonResDto<>("게시글 삭제", dto), HttpStatus.OK);
    }

    @GetMapping("/search/title")
    @Parameter(name = "keyword", description = "조회할 게시글의 제목", required = true)
    @Operation(summary = "제목으로 게시글 검색", description = "데이터베이스에서 키워드가 포함된 게시글 제목으로 검색합니다.")
    public ResponseEntity<List<BoardResDto>> searchBoardsByTitle(@RequestParam String keyword) {
        List<BoardResDto> searchBoards = boardService.searchBoardsByTitle(keyword);
        return new ResponseEntity<>(searchBoards, HttpStatus.OK);
    }

    @GetMapping("/status")
    @Operation(summary = "거래 상태별 게시글 조회", description = "특정 거래 상태(IN_PROGRESS, COMPLETED)의 게시글을 조회합니다.")
    public ResponseEntity<CommonResDto<List<BoardResDto>>> getBoardsByStatus(@RequestParam BoardStatus status) {
        List<BoardResDto> dto = boardService.getBoardsByStatus(status);
        return new ResponseEntity<>(new CommonResDto<>("거래 상태별 게시글 조회", dto), HttpStatus.OK);
    }

    @GetMapping("/filter")
    @Operation(summary = "카테고리별 거래 상태 게시글 조회", description = "특정 카테고리(GROUP, USED) 내에서 특정 거래 상태(IN_PROGRESS, COMPLETED)인 게시글을 조회합니다.")
    public ResponseEntity<CommonResDto<List<BoardResDto>>> getBoardsByCategoryAndStatus(@RequestParam BoardCategory category, @RequestParam BoardStatus status) {
        List<BoardResDto> dto = boardService.getBoardsByCategoryAndStatus(category, status);
        return new ResponseEntity<>(new CommonResDto<>("카테고리별 거래 상태 게시글 조회", dto), HttpStatus.OK);
    }

}