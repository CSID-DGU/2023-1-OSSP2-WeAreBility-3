//OtherUserModel.dart
class OtherUserModel {
  final String name;
  final String imagePath;
  final String introduction;
  final bool isPremium;
  final int likeCnt;
  final int cmtCnt;
  final int badgeCnt;
  final int followingCnt;
  final int followerCnt;

  OtherUserModel(
    this.name,
    this.imagePath,
    this.introduction,
    this.isPremium,
    this.likeCnt,
    this.cmtCnt,
    this.badgeCnt,
    this.followingCnt,
    this.followerCnt,
  );

  factory OtherUserModel.fromJson(Map<String, dynamic> json) {
    return OtherUserModel(
      json['name'],
      json['image_path'],
      json['introduction'],
      json['is_premium'],
      json['like_cnt'],
      json['comment_cnt'],
      json['badge_cnt'],
      json['following_cnt'],
      json['follower_cnt'],
    );
  }
}
