package com.soongsil.soongpal.board.controller;

import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.domain.BoardStatus;
import com.soongsil.soongpal.board.dto.BoardCreateReqDto;
import com.soongsil.soongpal.board.dto.BoardResDto;
import com.soongsil.soongpal.board.dto.BoardUpdateReqDto;
import com.soongsil.soongpal.board.dto.LikeResDto;
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
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "400", description = "유효하지 않은 파라미터 값", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Operation(method = "GET", summary = "게시글 목록 조회 및 필터링", description = "모든 게시글 또는 'keyword', 'category', 'status' 파라미터를 조합해 게시글을 조회합니다. 파라미터가 없으면 모든 게시글을 조회합니다.")
    @Parameter(name = "keyword", description = "조회할 게시글의 제목 (선택 사항)", required = false)
    @Parameter(name = "category", description = "조회할 게시글의 카테고리 (GROUP 또는 USED) (선택 사항)", required = false)
    @Parameter(name = "status", description = "조회할 게시글의 거래 상태 (IN_PROGRESS 또는 COMPLETED) (선택 사항)", required = false)
    public ResponseEntity<CommonResDto<List<BoardResDto>>> getBoards(@RequestParam(required = false) String keyword, @RequestParam(required = false) BoardCategory category, @RequestParam(required = false) BoardStatus status) {
        List<BoardResDto> dto = boardService.getFilteredBoards(keyword, category, status);
        return new ResponseEntity<>(new CommonResDto<>("게시글 조회 성공", dto), HttpStatus.OK);
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

    @PostMapping("/{id}/like")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = CommonResDto.class)))
    @Parameter(name = "id", description = "좋아요할 게시글의 ID", required = true)
    @Operation(method = "POST", summary = "게시글 좋아요 생성", description = "게시글 좋아요 생성하기 위한 API")
    public ResponseEntity<CommonResDto<LikeResDto>> addLike(@PathVariable Long id) {
        LikeResDto dto = boardService.addLike(id);
        return new ResponseEntity<>(new CommonResDto<>("게시글 좋아요 생성", dto), HttpStatus.OK);
    }

}