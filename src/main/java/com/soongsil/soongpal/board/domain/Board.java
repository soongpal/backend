package com.soongsil.soongpal.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Board {

    @Id
    private Long id;
    private String title;

}
