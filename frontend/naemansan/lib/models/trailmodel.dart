//등록한 산책로 api를 JSON형태로 변환
class TrailModel {
  final String userimage;
  final String title;
  final String startpoint; //시작 위치 불러오기  (Segment ID -startpoint)
  final double distance; //거리 (endpoint - startpoint)
  final List<String>? CourseKeyWord; //여러개일수도 있음
  final int likeCnt, userCnt;
  final bool isLiked;

  TrailModel.fromJson(Map<String, dynamic> json)
      : userimage = json['userimage'],
        title = json['title'],
        startpoint = json['startpoint'],
        distance = json['distance'],
        CourseKeyWord = (json['CourseKeyword'] as List<dynamic>).cast<String>(),
        likeCnt = json['likeCnt'],
        userCnt = json['userCnt'],
        isLiked = json['isLiked'];
}
