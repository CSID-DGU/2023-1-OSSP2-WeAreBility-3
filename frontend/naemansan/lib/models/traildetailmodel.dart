// traildetailmodel.dart
// 위치 값도 불러오게 - trailmodel.dart에 locations만 추가
// 아이디 값
class TraildetailModel {
  final int id;
  final String title;
  final DateTime createdDate;
  final List<String> tags;
  final String startLocationName;
  final double distance;
  final int likeCount;
  final int userCount;
  final bool isLiked;
  final List<Map<String, double>> locations;

  TraildetailModel({
    required this.id,
    required this.title,
    required this.createdDate,
    required this.tags,
    required this.startLocationName,
    required this.distance,
    required this.likeCount,
    required this.userCount,
    required this.isLiked,
    required this.locations,
  });

  factory TraildetailModel.fromJson(Map<String, dynamic> json) {
    return TraildetailModel(
      id: json['id'],
      title: json['title'],
      createdDate: DateTime.parse(json['created_date']),
      tags: List<String>.from(json['tags'].map((tag) => tag['name'])),
      startLocationName: json['start_location_name'],
      distance: json['distance'].toDouble(),
      likeCount: json['like_cnt'],
      userCount: json['using_unt'],
      isLiked: json['is_like'],
      locations:
          List<Map<String, double>>.from(json['locations'].map((location) => {
                'latitude': location['latitude'].toDouble(),
                'longitude': location['longitude'].toDouble(),
              })),
    );
  }
}

/*
course/enrollment/{id}
/course/individual/{id}
*/