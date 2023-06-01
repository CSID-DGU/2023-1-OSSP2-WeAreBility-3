package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name="notices")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name="created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "is_edit", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isEdit;

    @Column(name = "status", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean status;

    @Column(name = "read_cnt", nullable = false)
    private Long readCnt;

    @Builder
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.isEdit = true;
        this.status = true;
        this.readCnt = 0L;
    }

    public void incrementCnt() {
        readCnt++;
    }
}
