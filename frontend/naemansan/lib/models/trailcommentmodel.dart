//댓글단 산책로 위젯에서 정보 불러올 때 사용하는 모델
class TrailCommentModel {
  final int id;
  final int courseId;
  final String title;
  final String content;
  final List<String> tags;

  TrailCommentModel({
    required this.id,
    required this.courseId,
    required this.title,
    required this.content,
    required this.tags,
  });

  factory TrailCommentModel.fromJson(Map<String, dynamic> json) {
    return TrailCommentModel(
      id: json['id'],
      courseId: json['course_id'],
      title: json['course_title'],
      content: json['content'],
      tags: List<String>.from(json['tags'].map((tag) => tag['name'])),
    );
  }
}
