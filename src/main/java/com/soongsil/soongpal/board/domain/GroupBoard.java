package com.soongsil.soongpal.board.domain;

import com.soongsil.soongpal.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GroupBoard extends BaseEntity {

    @Id
    private Long id;
    private String title;

}
