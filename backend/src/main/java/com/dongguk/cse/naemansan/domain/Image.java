package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.ImageUseType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "images")
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
    @Column(name = "path", nullable = false)
    private String imagePath;

    @Builder
    public Image(Long useId, ImageUseType imageUseType) {
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
        this.imagePath = "어떠한 경로 입니다.";
    }
}
