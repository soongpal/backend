package com.soongsil.soongpal.board.domain;

import com.soongsil.soongpal.common.domain.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    private Integer price;

    private String url;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardStatus status;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImages = new ArrayList<>();

    public void addBoardImage(BoardImage boardImage) {
        this.boardImages.add(boardImage);
        boardImage.setBoard(this);
    }

    public void removeBoardImage(Long imageId) {
        this.boardImages.removeIf(image -> image.getId().equals(imageId));
    }

    public void update(String title, String content, Integer price, String url, String location,
                       BoardCategory category, BoardStatus status) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.url = url;
        this.location = location;
        this.category = category;
        this.status = status;
    }
}