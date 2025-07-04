package com.soongsil.soongpal.board.controller;

import com.soongsil.soongpal.board.dto.group.GroupCreateReqDto;
import com.soongsil.soongpal.board.dto.group.GroupResDto;
import com.soongsil.soongpal.board.service.GroupBoardService;
import com.soongsil.soongpal.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/group-purchases")
@RestController
public class GroupBoardController {

    private final GroupBoardService groupBoardService;

    @PostMapping
    public ResponseEntity<CommonResDto<GroupResDto>> createGroup(@Valid @RequestBody GroupCreateReqDto groupCreateReqDto) {
        GroupResDto dto = groupBoardService.createGroup(groupCreateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 생성", dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<List<GroupResDto>>> getAllGroups() {
        List<GroupResDto> dto = groupBoardService.getAllGroups();
        return new ResponseEntity<>(new CommonResDto<>("모든 공동구매 게시글 조회", dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto<GroupResDto>> getGroupById(@PathVariable Long id) {
        GroupResDto dto = groupBoardService.getGroupById(id);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 상세 조회", dto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResDto<GroupResDto>> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupCreateReqDto groupCreateReqDto) {
        GroupResDto dto = groupBoardService.updateGroup(id, groupCreateReqDto);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 수정", dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResDto<GroupResDto>> deleteGroup(@PathVariable Long id) {
        GroupResDto dto = groupBoardService.deleteGroup(id);
        return new ResponseEntity<>(new CommonResDto<>("공동구매 게시글 삭제", dto), HttpStatus.OK);
    }
}
