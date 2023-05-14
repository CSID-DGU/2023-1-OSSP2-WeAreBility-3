package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.domain.type.UserRoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.management.BadBinaryOpValueExpException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "social_login_id")
    private String socialLoginId;
    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private LoginProviderType loginProviderType;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;
    @Column(name = "name")
    private String name;
    @Column(name = "introduction")
    private String introduction;
    @Column(name = "created_date")
    private Timestamp createdDate;

    @OneToMany(mappedBy = "user")
    private List<Badge> badges = new ArrayList<>();

    @Builder
    public User(String socialLoginId, LoginProviderType loginProviderType, String name) {
        this.socialLoginId = socialLoginId;
        this.loginProviderType = loginProviderType;
        this.userRoleType = UserRoleType.USER;
        this.name = name;
        this.introduction = "안녕하세요!";
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
