import 'package:naemansan/models/trailmodel.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

// 토큰 얻어 오기
Future<String> getTokenFromServer(String code) async {
  // Make an HTTP request to your server to exchange the authorization code for an access token
  var response = await http.post(
    Uri.parse('YOUR_SERVER_TOKEN_ENDPOINT'),
    body: {
      'code': code,
    },
  );

  if (response.statusCode == 200) {
    // Parse the response to extract the access token
    var token = 'PARSE_ACCESS_TOKEN_FROM_RESPONSE';
    return token;
  } else {
    throw Exception('Failed to get token from server');
  }
}

class ApiService {
  //---------------------------------------산책로 페이지----------------------------------------------------------------------------------------
//---------------------------------------거리순 리스트 불러오기--------------------------------------------------------------------------------
  static Future<List<TrailModel>> getNearestTrail() async {
    List<TrailModel> NearestTrailInstances = [];
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/location?page={}&num={}&latitude={}&longitude={}"); //위치기반 산책로 조회 Api
    final response = await http.get(url);
    if (response.statusCode == 200) {
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
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/like?page={}&num={}"); //좋아요순
    final response = await http.get(url);
    if (response.statusCode == 200) {
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
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/using?page={}&num={}"); //이용자순 Api
    final response = await http.get(url);
    if (response.statusCode == 200) {
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
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/api/course/list?fliter={enum}"); //**전체 산책로 목록 조회
    final response = await http.get(url);
    if (response.statusCode == 200) {
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
    List<TrailModel> createdTrailInstances = [];
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/individual/enrollment?page={}&num={}");
    final response = await http.get(url);

    if (response.statusCode == 200) {
      final Map<String, dynamic> data = jsonDecode(response.body);
      final dynamic trails = data['trails'];

      if (trails != null && trails is List<dynamic>) {
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          createdTrailInstances.add(instance);
        }
      } else {
        print("등록한 산책로가 없습니다");
        return createdTrailInstances; // 산책로 정보가 없을 때 바로 반환
      }
      return createdTrailInstances;
    }
    /*else {
    throw Exception('Invalid status code: ${response.statusCode}');
  }*/
    else {
      print('정보를 불러올 수 없습니다');
      return createdTrailInstances;
    }
  }

  //-----------------------------------좋아요한 산책로 리스트 불러오기-----------------------------------------------------------------------
  static Future<List<TrailModel>> getLikedTrail() async {
    List<TrailModel> LikedTrailInstances = [];
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/individual/like?page={}&num={}");
    final response = await http.get(url);

    if (response.statusCode == 200) {
      final Map<String, dynamic> data = jsonDecode(response.body);
      final dynamic trails = data['trails'];

      if (trails != null && trails is List<dynamic>) {
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          LikedTrailInstances.add(instance);
        }
      } else {
        print("No trail information available.");
      }
    } /* else {
      throw Exception('Invalid status code: ${response.statusCode}');
    }*/

    //좋아요한 산책로가 없습니다 표기 추가
    return LikedTrailInstances;
  }

  //----------------------------------이용한 산책로 리스트 불러오기---------------------------------------------------------------------
  static Future<List<TrailModel>> getUsedTrail() async {
    List<TrailModel> UsedTrailInstances = [];
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/individual/using?page={}&num={}"); //사용한 산책로 조회
    final response = await http.get(url);
    if (response.statusCode == 200) {
      final Map<String, dynamic> data = jsonDecode(response.body);
      final dynamic trails = data['trails'];

      if (trails != null) {
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          UsedTrailInstances.add(instance);
        }
      } else {
        print("No trail information available.");
      }
    } /* else {
      throw Exception('Invalid status code: ${response.statusCode}');
    }*/
    //이용한 산책로가 없습니다 표기 추가
    return UsedTrailInstances;
  }

  //----------------------------------댓글단 산책로 리스트 불러오기---------------------------------------------------------------------
  static Future<List<TrailModel>> getCommentedTrail() async {
    List<TrailModel> CommentedTrailInstances = [];
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/user/comment"); // 산책로 댓글 조회 api
    final response = await http.get(url);
    if (response.statusCode == 200) {
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
  static Future<List<TrailModel>> GetfollowedKeyTrail() async {
    List<TrailModel> followedKeyTrailInstances = [];
    final url = Uri.parse(
        "https://ossp.dcs-hyungjoon.com/course/list/individual/tag?page={}&num={}&name={}"); // 산책로 댓글 조회 api
    final response = await http.get(url);
    if (response.statusCode == 200) {
      final trails = jsonDecode(response.body);
      for (var trail in trails) {
        final instance = TrailModel.fromJson(trail);
        followedKeyTrailInstances.add(instance);
      }
      return followedKeyTrailInstances; //스크롤을 내리면서 목록이 더 생김
    }
    throw Error();
  }
}
