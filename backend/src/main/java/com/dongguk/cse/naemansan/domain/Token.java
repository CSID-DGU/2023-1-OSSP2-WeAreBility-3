package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tokens")
@DynamicUpdate
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private User tokenUser;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public Token(User tokenUser, String refreshToken) {
        this.tokenUser = tokenUser;
        this.refreshToken = refreshToken;
    }
}
