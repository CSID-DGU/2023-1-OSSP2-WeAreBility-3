package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.ImageUseType;
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
@Table(name = "images")
@DynamicUpdate
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "use_user", nullable = true)
    @OneToOne(fetch = FetchType.LAZY)
    private User imageUser;

    @JoinColumn(name = "use_shop", nullable = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Shop imageShop;

    @JoinColumn(name = "use_advertisement", nullable = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Advertisement imageAdvertisement;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "uuid_name", nullable = false)
    private String uuidName;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "path", nullable = false)
    private String path;

    @Builder
    public Image(Object userObject, ImageUseType imageUseType, String originName, String uuidName, String type, String path) {
        switch (imageUseType) {
            case USER -> {
                this.imageUser = (User) userObject;
                this.imageShop = null;
                this.imageAdvertisement = null;
            }
            case SHOP -> {
                this.imageShop = (Shop) userObject;
                this.imageUser = null;
                this.imageAdvertisement = null;
            }
            case ADVERTISEMENT -> {
                this.imageAdvertisement = (Advertisement) userObject;
                this.imageShop = null;
                this.imageUser = null;
            }
        }
        this.originName = originName;
        this.uuidName = uuidName;
        this.type = type;
        this.path = path;
    }

    public void updateImage(String originName, String uuidName, String type, String path) {
        setOriginName(originName);
        setUuidName(uuidName);
        setPath(type);
        setType(path);
    }
}
