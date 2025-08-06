package com.soongsil.soongpal.board.service;

import com.soongsil.soongpal.board.domain.*;
import com.soongsil.soongpal.board.dto.*;
import com.soongsil.soongpal.board.repository.BoardRepository;
import com.soongsil.soongpal.board.repository.LikeRepository;
import com.soongsil.soongpal.common.file.S3Uploader;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final S3Uploader s3Uploader;

    public BoardResDto createBoard(BoardCreateReqDto boardCreateReqDto, List<MultipartFile> images) {
        Board board = BoardCreateReqDto.toEntity(boardCreateReqDto);
        boardRepository.save(board);

        // 이미지 파일이 이씅면
        if (images != null && !images.isEmpty()) {
            for (MultipartFile imageFile : images) {
                // S3에 이미지 업로드하고 "board" 디렉토리 아래에 저장
                String imageUrl = s3Uploader.uploadFile(imageFile, "board");
                if (imageUrl != null) {
                    BoardImage boardImage = BoardImage.builder()
                            .imageUrl(imageUrl)
                            .build();
                    board.addBoardImage(boardImage);
                }
            }
        }
        return BoardResDto.from(board);
    }

    public BoardResDto getBoardById(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return BoardResDto.from(findBoard);
    }

    @Transactional
    public BoardResDto updateBoard(Long id, @Valid BoardUpdateReqDto boardUpdateReqDto,
                                   List<MultipartFile> newImages, List<Long> deleteImageIds) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        findBoard.update(
                boardUpdateReqDto.getTitle(),
                boardUpdateReqDto.getContent(),
                boardUpdateReqDto.getPrice(),
                boardUpdateReqDto.getUrl(),
                boardUpdateReqDto.getLocation(),
                boardUpdateReqDto.getCategory(),
                boardUpdateReqDto.getStatus()
        );

        // 이미지 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long deleteImageId : deleteImageIds) {
                BoardImage imageToDelete = findBoard.getBoardImages().stream()
                        .filter(img -> img.getId().equals(deleteImageId))
                        .findFirst()
                        .orElse(null);

                if (imageToDelete != null) {
                    s3Uploader.deleteFile(imageToDelete.getImageUrl());
                    findBoard.removeBoardImage(deleteImageId);
                }
            }
        }

        // 새 이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile imageFile : newImages) {
                String imageUrl = s3Uploader.uploadFile(imageFile, "board");
                BoardImage newBoardImage = BoardImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                findBoard.addBoardImage(newBoardImage);
            }
        }

        return BoardResDto.from(findBoard);
    }

    public BoardResDto deleteBoard(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        findBoard.getBoardImages().forEach(image -> {
            s3Uploader.deleteFile(image.getImageUrl());
        });

        boardRepository.deleteById(id);
        return BoardResDto.from(findBoard);
    }

    public BoardPageResDto getFilteredBoards(String keyword, BoardCategory category, BoardStatus status, int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("createdTime").descending());
        Page<Board> boardsPage;

        if (keyword != null && !keyword.isEmpty()) {
            // 키워드로 게시글 제목 검색
            boardsPage = boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }
        else if (category != null && status != null) {
            // 카테고리 + 상태 조합 검색
            boardsPage = boardRepository.findByCategoryAndStatus(category, status, pageable);
        } else if (category != null) {
            // 게시글 카테고리 검색
            boardsPage = boardRepository.findByCategory(category, pageable);
        } else if (status != null) {
            // 현재 게시글 상태 검색
            boardsPage = boardRepository.findByStatus(status, pageable);
        } else {
            // 모든 게시글 조회
            boardsPage = boardRepository.findAll(pageable);
        }

        Page<BoardResDto> boardPageResDto = boardsPage.map(BoardResDto::from);
        return BoardPageResDto.from(boardPageResDto);
    }

    public LikeResDto addLike(Long boardId) {
        Like like = Like.builder()
                .boardId(boardId)
                .build();
        likeRepository.save(like);

        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }

    public LikeResDto deleteLike(Long boardId) {
        Like findLike = likeRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("좋아요 정보가 없습니다."));
        likeRepository.delete(findLike);

        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }

    public LikeResDto getLikeCount(Long boardId) {
        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }
}