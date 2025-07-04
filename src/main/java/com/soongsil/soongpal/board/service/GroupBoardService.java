package com.soongsil.soongpal.board.service;

import com.soongsil.soongpal.board.domain.GroupBoard;
import com.soongsil.soongpal.board.dto.group.GroupCreateReqDto;
import com.soongsil.soongpal.board.dto.group.GroupResDto;
import com.soongsil.soongpal.board.repository.GroupBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<GroupResDto> getAllGroups() {
        List<GroupBoard> boards = groupBoardRepository.findAll();
        return boards.stream()
                .map(GroupResDto::from)
                .toList();
    }

    public GroupResDto getGroupById(Long id) {
        GroupBoard findBoard = groupBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return GroupResDto.from(findBoard);
    }
}
