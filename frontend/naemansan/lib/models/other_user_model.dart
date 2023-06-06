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

  OtherUserModel({
    required this.name,
    required this.imagePath,
    required this.introduction,
    required this.isPremium,
    required this.likeCnt,
    required this.cmtCnt,
    required this.badgeCnt,
    required this.followingCnt,
    required this.followerCnt,
  });

  factory OtherUserModel.fromJson(Map<String, dynamic> json) {
    return OtherUserModel(
      name: json['name'],
      imagePath: json['image_path'],
      introduction: json['introduction'],
      isPremium: json['is_premium'],
      likeCnt: json['like_cnt'],
      cmtCnt: json['comment_cnt'],
      badgeCnt: json['badge_cnt'],
      followingCnt: json['following_cnt'],
      followerCnt: json['follower_cnt'],
    );
  }
}
