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
    @Column(name = "use_user", nullable = true)
    private Long userId;
    @Column(name = "use_shop", nullable = true)
    private Long shopId;
    @Column(name = "use_advertisement", nullable = true)
    private Long advertisementId;
    @Column(name = "origin_name", nullable = false)
    private String originName;
    @Column(name = "uuid_name", nullable = false)
    private String uuidName;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "path", nullable = false)
    private String path;

    @Builder
    public Image(Long useId, ImageUseType imageUseType, String originName, String uuidName, String type, String path) {
        switch (imageUseType) {
            case USER -> {
                this.userId = useId;
                this.shopId = this.advertisementId = null;
            }
            case SHOP -> {
                this.shopId = useId;
                this.userId = this.advertisementId = null;
            }
            case ADVERTISEMENT -> {
                this.advertisementId = useId;
                this.shopId = this.userId = null;
            }
        }
        this.originName = originName;
        this.uuidName = uuidName;
        this.type = type;
        this.path = path;
    }
}
