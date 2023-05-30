package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "advertisements")
@DynamicUpdate
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "enterprise_name")
    private String enterpriseName;

    @Column(name = "enterprise_url")
    private String enterpriseUrl;

    // ------------------------------------------------------------

    @OneToOne(mappedBy = "advertisement", fetch = FetchType.LAZY)
    private Image image;

    @Builder
    public Advertisement(String enterpriseName, String enterpriseUrl) {
        this.enterpriseName = enterpriseName;
        this.enterpriseUrl = enterpriseUrl;
    }
}
