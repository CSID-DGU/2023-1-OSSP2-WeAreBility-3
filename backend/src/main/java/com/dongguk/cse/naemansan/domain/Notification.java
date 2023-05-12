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
    @Column(name = "user_id")
    private Long user_id;
    @Column(name = "content")
    private String content;
    @Column(name = "create_date")
    private Timestamp create_date;
    @Column(name = "is_read_status", columnDefinition = "TINYINT(1)")
    private Boolean is_read_status;

    @Builder
    public Notification(Long user_id, String content) {
        this.user_id = user_id;
        this.content = content;
        this.create_date = Timestamp.valueOf(LocalDateTime.now());
        this.is_read_status = false;
    }
}
