class TrailModel {
  final int id;
  final String title;
  final DateTime createdDateTime;
  final List<Map<String, dynamic>> courseTags;
  final String startLocationName;
  final double distance;

  TrailModel({
    required this.id,
    required this.title,
    required this.createdDateTime,
    required this.courseTags,
    required this.startLocationName,
    required this.distance,
  });

  factory TrailModel.fromJson(Map<String, dynamic> json) {
    return TrailModel(
      id: json['id'],
      title: json['title'],
      createdDateTime: DateTime.parse(json['createdDateTime']),
      courseTags: List<Map<String, dynamic>>.from(json['courseTags']),
      startLocationName: json['startLocationName'],
      distance: json['distance'].toDouble(),
    );
  }
}
