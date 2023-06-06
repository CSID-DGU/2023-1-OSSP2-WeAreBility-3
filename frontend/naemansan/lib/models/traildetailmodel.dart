// traildetailmodel.dart
// 위치 값도 불러오게 - trailmodel.dart에 locations만 추가
// 아이디 값
// 산책로 세부 페이지 볼 때 사용하는 모델
class TraildetailModel {
  final int id;
  final int userId;
  final String userName;
  final String title;
  final DateTime createdDate;
  final String introduction;
  final List<String> tags;
  final String startLocationName;
  final List<Map<String, double>> locations;
  final double distance;
  //final int likeCount;
  //final int userCount;
  final bool isLiked;

  TraildetailModel({
    required this.id,
    required this.userId,
    required this.userName,
    required this.title,
    required this.createdDate,
    required this.introduction,
    required this.tags,
    required this.startLocationName,
    required this.locations,
    required this.distance,
    //required this.likeCount,
    //required this.userCount,
    required this.isLiked,
  });

  factory TraildetailModel.fromJson(Map<String, dynamic> json) {
    return TraildetailModel(
      id: json['id'],
      userId: json['user_id'],
      userName: json['user_name'],
      title: json['title'],
      createdDate: DateTime.parse(json['created_date']),
      introduction: json['introduction'],
      tags: List<String>.from(json['tags'].map((tag) => tag['name'])),
      startLocationName: json['start_location_name'],
      //likeCount: json['like_cnt'],
      //userCount: json['using_unt'],
      locations:
          List<Map<String, double>>.from(json['locations'].map((location) => {
                'latitude': location['latitude'].toDouble(),
                'longitude': location['longitude'].toDouble(),
              })),
      distance: json['distance'].toDouble(),
      isLiked: json['is_like'],
    );
  }
}

/*
course/enrollment/{id}
/course/individual/{id}
*/
