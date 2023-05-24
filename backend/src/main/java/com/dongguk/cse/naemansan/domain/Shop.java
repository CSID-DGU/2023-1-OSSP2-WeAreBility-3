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
@Table(name = "shops")
@DynamicUpdate
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_introduction")
    private String shopIntroduction;

    @Column(name = "shop_location")
    private Point shopLocation;

    // ------------------------------------------------------------

    @OneToOne(mappedBy = "shop", fetch = FetchType.LAZY)
    private Image image;

    @Builder
    public Shop(String shopName, String shopIntroduction, Point shopLocation) {
        this.shopName = shopName;
        this.shopIntroduction = shopIntroduction;
        this.shopLocation = shopLocation;
    }
}
