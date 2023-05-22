package com.dongguk.cse.naemansan.dto.response;

import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String name;
    private String image_path;
    private String introduction;
    private Boolean is_premium;
    private Long like_cnt;
    private Long comment_cnt;
    private Long badge_cnt;
    private Long following_cnt;
    private Long follower_cnt;

    @Builder
    public UserDto(User user, Image image, Boolean is_premium, Long like_cnt, Long comment_cnt, Long badge_cnt, Long following_cnt, Long follower_cnt) {
        this.name = user.getName();
        this.image_path = image.getUuidName();
        this.introduction = user.getIntroduction();
        this.is_premium = is_premium;
        this.like_cnt = like_cnt;
        this.comment_cnt = comment_cnt;
        this.badge_cnt = badge_cnt;
        this.following_cnt = following_cnt;
        this.follower_cnt = follower_cnt;
    }
}