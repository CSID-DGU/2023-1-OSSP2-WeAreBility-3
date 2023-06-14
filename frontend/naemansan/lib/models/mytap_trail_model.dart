//MytabTrail.dart
//산책로 목록으로 볼 때 사용하는 모델
class MytabTrailModel {
  final int id;
  final String title;
  final DateTime createdDate;
  final double distance;

  MytabTrailModel({
    required this.id,
    required this.title,
    required this.createdDate,
    required this.distance,
  });

  factory MytabTrailModel.fromJson(Map<String, dynamic> json) {
    return MytabTrailModel(
      id: json['id'],
      title: json['title'],
      createdDate: DateTime.parse(json['created_date']),
      distance: json['distance'].toDouble(),
    );
  }
}
