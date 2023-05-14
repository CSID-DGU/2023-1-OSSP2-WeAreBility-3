import 'package:naemansan/models/trailmodel.dart';
//http
import 'dart:convert';

class ApiService {
  static Future<List<TrailModel>> getTrail() async {
    List<TrailModel> trailInstances = [];

    // 테스트 데이터로 주어진 JSON 문자열을 파싱하여 TrailModel 인스턴스를 생성하여 리스트에 추가합니다.
    String jsonData = '''
    {
      "userimage": "image",
      "title": "중구 산책길",
      "startpoint": "서울특별시 중구",
      "distance": 0.8,
      "CourseKeyword": ["둘레길"],
      "likeCnt": 10,
      "userCnt": 32,
      "isLiked": true 
    }
    ''';
    TrailModel instance = TrailModel.fromJson(jsonDecode(jsonData));
    trailInstances.add(instance);

    return trailInstances;
  }
}

/*
//---------------------------------------산책로 페이지----------------------------------------------------------------------------------------
//---------------------------------------거리순 리스트 불러오기--------------------------------------------------------------------------------
  static Future<List<TrailModel>> getNearestTrail() async {
    List<TrailModel> NearestTrailInstances = [];
    final url = Uri.parse("/api/course/{location}/top5"); //위치기반 산책로 top5 Api
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        NearestTrailInstances.add(instance);
      }
      return NearestTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }

//---------------------------------------좋아요순 리스트 불러오기------------------------------------------------------------------------------
  static Future<List<TrailModel>> getMostLikedTrail() async {
    List<TrailModel> MostLikedTrailInstances = [];
    final url = Uri.parse("/api/course/total/top5"); //위치기반 산책로 top5 Api
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        MostLikedTrailInstances.add(instance);
      }
      return MostLikedTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }

//---------------------------------------이용자순 리스트 불러오기---------------------------------------------------------------------------
  static Future<List<TrailModel>> getMostUsedTrail() async {
    List<TrailModel> MostUsedTrailInstances = [];
    final url = Uri.parse("/api/course/total/top5"); //이용자순 Api
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        MostUsedTrailInstances.add(instance);
      }
      return MostUsedTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }

//---------------------------------------최신(등록)순 리스트 불러오기-----------------------------------------------------------------------
  static Future<List<TrailModel>> getNewTrail() async {
    List<TrailModel> NewTrailInstances = [];
    final url = Uri.parse("/api/course/total/top5"); //**최신순 Api로 변경
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        NewTrailInstances.add(instance);
      }
      return NewTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }
//---------------------------------------키워드별 리스트 불러오기------------------------------------
//
//
//
//

//--------------------------------------나만의 산책로 페이지-----------------------------------------
//------------------------------------등록한 산책로 리스트 불러오기-----------------------------------------------
  static Future<List<TrailModel>> getCreatedTrail() async {
    List<TrailModel> CreatedTrailInstances = [];
    final url = Uri.parse("/api/course"); //등록한 산책로 api
    final response = await http.get(url);
    if (response.statusCode == 201) {
      try {
        final trails = jsonDecode(response.body);
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          CreatedTrailInstances.add(instance);
        }
        return CreatedTrailInstances; //스크롤을 내리면서 목록이 더 생김
      } catch (e) {
        print('Failed to decode trails: $e');
        rethrow;
      }
    } else {
      throw Error();
    }
  }

  //-----------------------------------좋아요한 산책로 리스트 불러오기-----------------------------------------------------------------------
  static Future<List<TrailModel>> getLikedTrail() async {
    List<TrailModel> LikedTrailInstances = [];
    final url = Uri.parse("/api/my_course/liked"); //좋아요한 산책로 api
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        LikedTrailInstances.add(instance);
      }
      return LikedTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }

  //----------------------------------이용한 산책로 리스트 불러오기---------------------------------------------------------------------
  static Future<List<TrailModel>> getUsedTrail() async {
    List<TrailModel> UsedTrailInstances = [];
    final url = Uri.parse("/api/my_course/finished"); //이용한 산책로 api
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        UsedTrailInstances.add(instance);
      }
      return UsedTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }

  //----------------------------------댓글단 산책로 리스트 불러오기---------------------------------------------------------------------
  static Future<List<TrailModel>> getCommentedTrail() async {
    List<TrailModel> CommentedTrailInstances = [];
    final url = Uri.parse("/api/course/comments"); // *****
    final response = await http.get(url);
    if (response.statusCode == 201) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        CommentedTrailInstances.add(instance);
      }
      return CommentedTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }

//-----------------------------------키워드별 리스트 불러오기---------------------------------------------------------------------------------
//
//
//
//   
*/ */


