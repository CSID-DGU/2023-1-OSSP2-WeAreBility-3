class TrailCommentModel {
  final int id;
  final int userId;
  final int courseId;
  final String userName;
  final String content;
  final DateTime createdDate;
  final bool isEdit;

  TrailCommentModel({
    required this.id,
    required this.userId,
    required this.courseId,
    required this.userName,
    required this.content,
    required this.createdDate,
    required this.isEdit,
  });

  factory TrailCommentModel.fromJson(Map<String, dynamic> json) {
    return TrailCommentModel(
      id: json['id'],
      userId: json['user_id'],
      courseId: json['course_id'],
      userName: json['user_name'],
      content: json['content'],
      createdDate: DateTime.parse(json['created_date']),
      isEdit: json['is_edit'],
    );
  }
}
