package com.soongsil.soongpal.board.service;

import com.soongsil.soongpal.board.domain.GroupBoard;
import com.soongsil.soongpal.board.dto.group.GroupCreateReqDto;
import com.soongsil.soongpal.board.dto.group.GroupResDto;
import com.soongsil.soongpal.board.repository.GroupBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupBoardService {

    private final GroupBoardRepository groupBoardRepository;

    public GroupResDto createGroup(GroupCreateReqDto groupCreateReqDto) {
        GroupBoard board = GroupCreateReqDto.toEntity(groupCreateReqDto);
        groupBoardRepository.save(board);
        return GroupResDto.from(board);
    }
}
