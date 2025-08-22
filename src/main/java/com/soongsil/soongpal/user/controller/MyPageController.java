package com.soongsil.soongpal.user.controller;

import com.soongsil.soongpal.board.dto.BoardPageResDto;
import com.soongsil.soongpal.common.dto.CommonErrorDto;
import com.soongsil.soongpal.common.dto.CommonResDto;
import com.soongsil.soongpal.user.dto.InfoResDto;
import com.soongsil.soongpal.user.dto.InfoUpdateReqDto;
import com.soongsil.soongpal.user.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my-page")
@Tag(name = "MyPage Controller", description = "마이페이지로 사용자의 정보 Controller 입니다.")
public class MyPageController {

    private final MyPageService myPageService;


    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사옹자 정보 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자의 데이터 조회", content = @Content(schema = @Schema(implementation = CommonErrorDto.class)))
    })
    @GetMapping("/info")
    public ResponseEntity<CommonResDto<InfoResDto>> getInfo() {
        Long userId = getUserId();
        InfoResDto dto = myPageService.getInfo(userId);
        return new ResponseEntity<>(new CommonResDto<>("유저 정보 조회", dto), HttpStatus.OK);
    }

    @Operation(summary = "내 정보 수정", description = "닉네임, 프로필 등 유저 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자의 데이터 조회", content = @Content(schema = @Schema(implementation = CommonErrorDto.class)))
    })
    @PutMapping("/info")
    public ResponseEntity<CommonResDto<InfoResDto>> updateInfo(@RequestBody InfoUpdateReqDto infoUpdateReqDto) {
        Long userId = getUserId();
        InfoResDto dto = myPageService.updateInfo(userId, infoUpdateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("유저 정보 수정", dto), HttpStatus.OK);
    }

    @Operation(summary = "좋아요한 게시글 조회", description = "로그인한 사용자가 좋아요한 게시글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요한 게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자의 데이터 조회", content = @Content(schema = @Schema(implementation = CommonErrorDto.class)))
    })
    @GetMapping("/like")
    public ResponseEntity<CommonResDto<BoardPageResDto>> getLikedBoards(@RequestParam(defaultValue = "0") int page) {
        Long userId = getUserId();
        BoardPageResDto dto = myPageService.getLikedBoards(userId, page);
        return new ResponseEntity<>(new CommonResDto<>("좋아요한 게시글 조회 성공", dto), HttpStatus.OK);
    }

    @Operation(summary = "내가 쓴 글 조회", description = "로그인한 사용자가 작성한 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작성한 게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자의 데이터 조회", content = @Content(schema = @Schema(implementation = CommonErrorDto.class)))
    })
    @GetMapping("/posts")
    public ResponseEntity<CommonResDto<BoardPageResDto>> getMyBoards(@RequestParam(defaultValue = "0") int page) {
        Long userId = getUserId();
        BoardPageResDto dto = myPageService.getMyBoards(userId, page);
        return new ResponseEntity<>(new CommonResDto<>("작성한 게시글 조회 성공", dto), HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 계정을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "계정 삭제 성공", content = @Content(schema = @Schema(implementation = CommonResDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자의 데이터 조회", content = @Content(schema = @Schema(implementation = CommonErrorDto.class)))
    })
    @DeleteMapping("/info")
    public ResponseEntity<CommonResDto<String>> deleteMyAccount() {
        Long userId = getUserId();
        String dto = myPageService.deleteMyAccount(userId);
        return new ResponseEntity<>(new CommonResDto<>("계정 삭제", dto), HttpStatus.OK);
    }

    public Long getUserId() {
        return 1L;
    }
}
