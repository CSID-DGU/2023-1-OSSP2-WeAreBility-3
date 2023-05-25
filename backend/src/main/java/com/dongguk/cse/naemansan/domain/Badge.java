package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "badge_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BadgeName badgeName;

    @Column(name = "get_date")
    private Timestamp getDate;

    @Builder
    public Badge(User user, BadgeName badgeName) {
        this.user = user;
        this.badgeName = badgeName;
        this.getDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
