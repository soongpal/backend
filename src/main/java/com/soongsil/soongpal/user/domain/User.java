package com.soongsil.soongpal.user.domain;

import com.soongsil.soongpal.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickName;

    private boolean deleted = false;

    public void changeInfo(String nickName) {
        this.nickName = nickName;
    }

    public void delete() {
        this.nickName = null;
        this.deleted = true;
    }

}
