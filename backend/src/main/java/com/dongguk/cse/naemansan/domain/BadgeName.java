package com.dongguk.cse.naemansan.domain;

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

    @OneToMany(mappedBy = "badgeName")
    private List<Badge> badges = new ArrayList<>();

    @Builder
    public BadgeName(String name) {
        this.name = name;
    }
}
