package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.domain.type.UserRoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.Times;
import org.hibernate.annotations.DynamicUpdate;

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

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private LoginProviderType loginProviderType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "is_login", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isLogin;

    @Column(name = "refresh_Token")
    private String refreshToken;

    @Column(name = "device_Token")
    private String deviceToken;

    @Column(name = "isIOS", columnDefinition = "TINYINT(1)")
    private Boolean isIos;

    @Column(name = "isPremium", columnDefinition = "TINYINT(1)")
    private Boolean isPremium;

    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    // ------------------------------------------------------------

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Image image;

    @OneToMany(mappedBy = "followingUser", fetch = FetchType.LAZY)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser", fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Badge> badges = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserTag> userTags = new ArrayList<>();

    // ------------------------------------------------------------

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<IndividualCourse> individualCourses = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<EnrollmentCourse> enrollmentCourses = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UsingCourse> usingCourses = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public User(String socialId, LoginProviderType loginProviderType, String name, UserRoleType userRoleType,
                String refreshToken) {
        this.socialId = socialId;
        this.loginProviderType = loginProviderType;
        this.userRoleType = userRoleType;
        this.name = name;
        this.introduction = "안녕하세요!";
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.isLogin = true;
        this.refreshToken = refreshToken;
        this.deviceToken = null;
        this.isIos = null;
        this.isPremium = null;
        this.expirationDate = null;
    }

    public void updateUser(String name, String introduction) {
        setName(name);
        setIntroduction(introduction);
    }

    public void updateDevice(String deviceToken, Boolean isIos) {
        setDeviceToken(deviceToken);
        setIsIos(isIos);
    }

    public void logoutUser() {
        setIsLogin(false);
        setRefreshToken(null);
        setDeviceToken(null);
    }

    public void updatePremium(Long monthCnt) {
        if (monthCnt == 0) {
            setIsPremium(null);
            setExpirationDate(null);
        } else {
            setIsPremium(true);
            setExpirationDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(monthCnt)));
        }
    }
}
