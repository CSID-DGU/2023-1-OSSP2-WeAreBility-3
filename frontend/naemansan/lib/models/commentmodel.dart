//trailmodel.dart
//산책로 목록으로 볼 때 사용하는 모델
class CommentModel {
  final int id;
  final int user_id;
  final int course_id;
  final String user_name;
  final String content;
  final DateTime created_date;
  final bool is_edit;

  CommentModel({
    required this.id,
    required this.user_id,
    required this.course_id,
    required this.user_name,
    required this.content,
    required this.created_date,
    required this.is_edit,
  });

  factory CommentModel.fromJson(Map<String, dynamic> json) {
    return CommentModel(
      id: json['id'],
      user_id: json['user_id'],
      course_id: json['course_id'],
      user_name: json['user_name'],
      content: json['content'],
      created_date: DateTime.parse(json['created_date']),
      is_edit: json['is_edit'],
    );
  }
}
