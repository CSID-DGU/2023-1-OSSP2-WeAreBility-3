package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "following_user_id")
    private Long followingId;
    @Column(name = "followed_user_id")
    private Long followedId;
    @Column(name = "created_at")
    private Timestamp createdDate;

    @Builder
    public Follow(Long id, Long followingId, Long followedId, Timestamp createdDate) {
        this.id = id;
        this.followingId = followingId;
        this.followedId = followedId;
        this.createdDate = createdDate;
    }
}
