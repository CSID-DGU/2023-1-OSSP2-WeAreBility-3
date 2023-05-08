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

//    @Builder
//    public UserDto(String name, String imagePath, String introduction, boolean isPremium, Long likeCnt, Long commentCnt, Long badgeCnt) {
//        this.name = name;
//        this.imagePath = imagePath;
//        this.introduction = introduction;
//        this.isPremium = isPremium;
//        this.likeCnt = likeCnt;
//        this.commentCnt = commentCnt;
//        this.badgeCnt = badgeCnt;
//    }

    @Builder
    public UserDto(User user, Image image, Long likeCnt, boolean isPremium, Long commentCnt, Long badgeCnt, Long followingCnt, Long followerCnt) {
        this.name = user.getName();
        this.imagePath = image.getImagePath();
        this.introduction = user.getIntroduction();
        this.isPremium = isPremium;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.badgeCnt = badgeCnt;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
    }
}