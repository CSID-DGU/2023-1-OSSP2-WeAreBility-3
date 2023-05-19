package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "following_user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User followingUser;

    @JoinColumn(name = "followed_user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User followerUser;

    @Column(name = "created_at")
    private Timestamp createdDate;

    @Builder
    public Follow(User followingUser, User followerUser) {
        this.followingUser = followingUser;
        this.followerUser = followerUser;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
