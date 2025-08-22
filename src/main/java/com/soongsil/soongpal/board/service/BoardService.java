package com.soongsil.soongpal.board.service;

import com.soongsil.soongpal.board.domain.*;
import com.soongsil.soongpal.board.dto.*;
import com.soongsil.soongpal.board.repository.BoardImageRepository;
import com.soongsil.soongpal.board.repository.BoardRepository;
import com.soongsil.soongpal.board.repository.LikeRepository;
import com.soongsil.soongpal.common.file.S3Uploader;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final BoardImageRepository boardImageRepository;

    public BoardResDto createBoard(BoardCreateReqDto boardCreateReqDto, List<MultipartFile> images, Long userId) {
        if (images != null && images.size() > 5) {
            throw new IllegalArgumentException("이미지는 최대 5개까지 업로드할 수 있습니다.");
        }

        User findUser = getUser(userId);
        Board board = BoardCreateReqDto.toEntity(boardCreateReqDto, findUser);
        boardRepository.save(board);

        // 이미지 파일이 이씅면
        if (images != null && !images.isEmpty()) {
            for (MultipartFile imageFile : images) {
                // S3에 이미지 업로드하고 "board" 디렉토리 아래에 저장
                String imageUrl = s3Uploader.uploadFile(imageFile, "board");
                if (imageUrl != null) {
                    BoardImage boardImage = BoardImage.builder()
                            .imageUrl(imageUrl)
                            .board(board)
                            .build();
                    boardImageRepository.save(boardImage);
                    board.addBoardImage(boardImage);
                }
            }
        }

        return BoardResDto.from(board, 0, false);
    }

    public BoardResDto getBoardById(Long id, Long userId) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Integer likeCount = likeRepository.countByBoardId(findBoard.getId());
        boolean liked = likeRepository.existsByBoardIdAndUserId(findBoard.getId(), userId);
        return BoardResDto.from(findBoard, likeCount, liked);
    }

    @Transactional
    public BoardResDto updateBoard(Long id, @Valid BoardUpdateReqDto boardUpdateReqDto,
                                   List<MultipartFile> newImages, List<Long> deleteImageIds, Long userId) {
        User findUser = getUser(userId);
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!findUser.equals(findBoard.getUser())) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        int currentImageCount = findBoard.getBoardImages().size();
        int deleteImageCount = (deleteImageIds != null) ? deleteImageIds.size() : 0;
        int newImageCount = (newImages != null) ? newImages.size() : 0;

        if (currentImageCount - deleteImageCount + newImageCount > 5) {
            throw new IllegalArgumentException("이미지는 최대 5개까지 업로드할 수 있습니다.");
        }

        findBoard.update(
                boardUpdateReqDto.getTitle(),
                boardUpdateReqDto.getContent(),
                boardUpdateReqDto.getPrice(),
                boardUpdateReqDto.getUrl(),
                boardUpdateReqDto.getLocation(),
                boardUpdateReqDto.getCategory()
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

        Integer likeCount = likeRepository.countByBoardId(findBoard.getId());
        boolean liked = likeRepository.existsByBoardIdAndUserId(findBoard.getId(), userId);
        return BoardResDto.from(findBoard, likeCount, liked);
    }

    @Transactional
    public BoardResDto updateBoardStatus(Long id, BoardStatusUpdateDto statusUpdateDto, Long userId) {
        User findUser = getUser(userId);
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!findUser.equals(findBoard.getUser())) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        findBoard.updateStatus(statusUpdateDto.getStatus());

        Integer likeCount = likeRepository.countByBoardId(findBoard.getId());
        boolean liked = likeRepository.existsByBoardIdAndUserId(findBoard.getId(), userId);
        return BoardResDto.from(findBoard, likeCount, liked);
    }

    public BoardResDto deleteBoard(Long id, Long userId) {
        User findUser = getUser(userId);
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if (!findUser.equals(findBoard.getUser())) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        findBoard.getBoardImages().forEach(image -> s3Uploader.deleteFile(image.getImageUrl()));

        boardRepository.deleteById(id);
        return BoardResDto.from(findBoard, 0, false);
    }

    public BoardPageResDto getFilteredBoards(String keyword, Long userId, BoardCategory category, BoardStatus status, int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("createdAt").descending());
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

        Page<BoardResDto> boardPageResDto = boardsPage.map(board -> BoardResDto.from(board, likeRepository.countByBoardId(board.getId()), likeRepository.existsByBoardIdAndUserId(board.getId(), userId) ));
        return BoardPageResDto.from(boardPageResDto);
    }

    public LikeResDto addLike(Long boardId, Long userId) {
        User findUser = getUser(userId);
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        if (likeRepository.existsByBoardIdAndUserId(findBoard.getId(), userId)) {
            int likeCount = likeRepository.countByBoardId(boardId);
            return LikeResDto.of(boardId, likeCount);
        }
        Like like = Like.builder()
                .board(findBoard)
                .user(findUser)
                .build();
        likeRepository.save(like);

        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }

    public LikeResDto deleteLike(Long boardId, Long userId) {
        Like findLike = likeRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new EntityNotFoundException("좋아요 정보가 없습니다."));
        likeRepository.delete(findLike);

        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }

    public LikeResDto getLikeCount(Long boardId) {
        int likeCount = likeRepository.countByBoardId(boardId);
        return LikeResDto.of(boardId, likeCount);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }
}