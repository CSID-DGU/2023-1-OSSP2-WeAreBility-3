package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notifications")
@DynamicUpdate
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User notificationUser;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "is_read_status", columnDefinition = "TINYINT(1)")
    private Boolean isReadStatus;

    @Builder
    public Notification(User notificationUser, String content) {
        this.notificationUser = notificationUser;
        this.content = content;
        this.createDate = Timestamp.valueOf(LocalDateTime.now());
        this.isReadStatus = false;
    }
}
