// 개인 산책로에 대한 세부정보를 불러올 때 사용하는 모델
/*
/course/individual/{id}
*/
class IndivTraildetailModel {
  final int id;
  final String title;
  final List<Map<String, dynamic>> locations;
  final DateTime createdDate;
  final double distance;

  IndivTraildetailModel({
    required this.id,
    required this.title,
    required this.createdDate,
    required this.locations,
    required this.distance,
  });

  factory IndivTraildetailModel.fromJson(Map<String, dynamic> json) {
    return IndivTraildetailModel(
      id: json['id'],
      title: json['title'],
      locations:
          List<Map<String, dynamic>>.from(json['locations'].map((location) {
        return {
          'latitude': location['latitude'],
          'longitude': location['longitude']
        };
      })),
      createdDate: DateTime.parse(json['created_date']),
      distance: json['distance'],
    );
  }
}
