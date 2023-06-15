class FollowModel {
  final int user_id;
  final String user_name;

  FollowModel({
    required this.user_id,
    required this.user_name,
  });

  factory FollowModel.fromJson(Map<String, dynamic> json) {
    return FollowModel(
      user_id: json['user_id'] as int,
      user_name: json['user_name'] as String,
    );
  }
}
