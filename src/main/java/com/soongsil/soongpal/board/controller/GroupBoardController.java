package com.soongsil.soongpal.board.controller;

import com.soongsil.soongpal.board.dto.group.GroupCreateReqDto;
import com.soongsil.soongpal.board.dto.group.GroupResDto;
import com.soongsil.soongpal.board.service.GroupBoardService;
import com.soongsil.soongpal.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/group-purchases")
@RestController
public class GroupBoardController {

    private final GroupBoardService groupBoardService;

    @PostMapping
    public ResponseEntity<CommonResDto<GroupResDto>> createGroup(@Valid @RequestBody GroupCreateReqDto groupCreateReqDto) {
        GroupResDto dto = groupBoardService.createGroup(groupCreateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시판 생성", dto), HttpStatus.CREATED);
    }


}
