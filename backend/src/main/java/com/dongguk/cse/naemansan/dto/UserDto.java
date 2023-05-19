package com.dongguk.cse.naemansan.dto;

import com.dongguk.cse.naemansan.domain.Image;
import com.dongguk.cse.naemansan.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String name;
    private String imagePath;
    private String introduction;
    private boolean isPremium;
    private Long likeCnt;
    private Long commentCnt;
    private Long badgeCnt;
    private Long followingCnt;
    private Long followerCnt;

    @Builder
    public UserDto(User user, Image image, Long likeCnt, boolean isPremium, Long commentCnt, Long badgeCnt, Long followingCnt, Long followerCnt) {
        this.name = user.getName();
        this.imagePath = image.getPath();
        this.introduction = user.getIntroduction();
        this.isPremium = isPremium;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.badgeCnt = badgeCnt;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
    }
}