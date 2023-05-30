package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.BadgeType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name="badge_names")
public class BadgeName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BadgeType type;

    @Column(name = "condition_num")
    private Long conditionNum;

    // ------------------------------------------------------------

    @OneToMany(mappedBy = "badgeName", fetch = FetchType.LAZY)
    private List<Badge> badges = new ArrayList<>();

    @Builder
    public BadgeName(String name) {
        this.name = name;
    }
}
