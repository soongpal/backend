package com.soongsil.soongpal.user.controller;

import com.soongsil.soongpal.board.dto.BoardPageResDto;
import com.soongsil.soongpal.common.dto.CommonResDto;
import com.soongsil.soongpal.user.dto.InfoResDto;
import com.soongsil.soongpal.user.dto.InfoUpdateReqDto;
import com.soongsil.soongpal.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my-page")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/info")
    public ResponseEntity<CommonResDto<InfoResDto>> getInfo() {
        Long userId = getUserId();
        InfoResDto dto = myPageService.getInfo(userId);
        return new ResponseEntity<>(new CommonResDto<>("유저 정보 조회", dto), HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<CommonResDto<InfoResDto>> updateInfo(@RequestBody InfoUpdateReqDto infoUpdateReqDto) {
        Long userId = getUserId();
        InfoResDto dto = myPageService.updateInfo(userId, infoUpdateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("유저 정보 수정", dto), HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<CommonResDto<BoardPageResDto>> getLikedBoards(@RequestParam(defaultValue = "0") int page) {
        Long userId = getUserId();
        BoardPageResDto dto = myPageService.getLikedBoards(userId, page);
        return new ResponseEntity<>(new CommonResDto<>("좋아요한 게시글 조회 성공", dto), HttpStatus.OK);
    }

    public Long getUserId() {
        return 1L;
    }
}
