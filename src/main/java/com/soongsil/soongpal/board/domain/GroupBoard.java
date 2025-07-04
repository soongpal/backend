package com.soongsil.soongpal.board.domain;

import com.soongsil.soongpal.common.domain.BaseEntity;
import jakarta.persistence.*;

@Entity

public class GroupBoard extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    private String url;

    private String location;
}
