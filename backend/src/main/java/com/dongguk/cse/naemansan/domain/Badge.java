package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "badge_id")
    private Long badgeId;
    @Column(name = "get_date")
    private Timestamp getDate;

    @Builder
    public Badge(Long userId, Long badgeId) {
        this.userId = userId;
        this.badgeId = badgeId;
        this.getDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
