package com.soongsil.soongpal.board.domain;

import com.soongsil.soongpal.common.domain.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

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

    private String url;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardStatus status;

    public void update(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.url = board.getUrl();
        this.location = board.getLocation();
    }
}