//등록한 산책로 api를 JSON형태로 변환
class TrailModel {
  //userimage
  final String title;
  //시작 위치 불러오기  (Segment ID -startpoint)
  //거리 (endpoint - startpoint)
  final List<String>? CourseKeyWord; //여러개일수도 있음
  //좋아요 수
  //이용자 수

  TrailModel.fromJson(Map<String, dynamic> json)
      : title = json['title'],
        //시작위치,
        //거리,
        CourseKeyWord = json['CourseKeyword'];
  //좋아요 수
  //이용자 수
}
