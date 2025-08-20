package com.soongsil.soongpal.user.controller;

import com.soongsil.soongpal.common.dto.CommonResDto;
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

    @PutMapping("/info")
    public ResponseEntity<CommonResDto<String>> updateInfo(@RequestBody InfoUpdateReqDto infoUpdateReqDto) {
        Long userId = getUserId();
        String dto = myPageService.updateInfo(userId, infoUpdateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("유저 정보 수정", dto), HttpStatus.OK);
    }

    public Long getUserId() {
        return 1L;
    }
}
