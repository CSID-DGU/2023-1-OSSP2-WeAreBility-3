package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="user_tags")
public class UserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "tag", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseTagType tag;

    @Builder
    public UserTag(User user, CourseTagType tag) {
        this.user = user;
        this.tag = tag;
    }
}