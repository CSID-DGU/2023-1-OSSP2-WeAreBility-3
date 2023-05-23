package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.PayType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "subscribes")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(name = "created_at")
    private Time createdDate;

    @Column(name = "successed_at")
    private Time successedDate;

    @Column(name = "expiration_date")
    private Time expirationDate;

    @Column(name = "next_order_date")
    private Time nextOrderDate;

    @Column(name = "biliing_key")
    private Time biliingKey;

    @Column(name = "next_refresh", columnDefinition = "TINYINT(1)")
    private Boolean nextRefresh;

    @Builder
    public Subscribe(User user, PayType payType, Time createdDate, Time successedDate, Time expirationDate, Time nextOrderDate, Time biliingKey, Boolean nextRefresh) {
        this.user = user;
        this.payType = payType;
        this.createdDate = createdDate;
        this.successedDate = successedDate;
        this.expirationDate = expirationDate;
        this.nextOrderDate = nextOrderDate;
        this.biliingKey = biliingKey;
        this.nextRefresh = nextRefresh;
    }
}
