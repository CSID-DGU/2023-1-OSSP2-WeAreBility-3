package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
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
    public Badge(Long id, Long userId, Long badgeId, Timestamp getDate) {
        this.id = id;
        this.userId = userId;
        this.badgeId = badgeId;
        this.getDate = getDate;
    }
}
