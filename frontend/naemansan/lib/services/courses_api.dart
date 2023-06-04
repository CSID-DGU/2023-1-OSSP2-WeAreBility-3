//courses_api.dart
//산책로 탭, 나만의 산책로 탭에서 산책로 목록을 불러올 때 사용
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'package:naemansan/models/trailmodel.dart';
import 'package:naemansan/models/traildetailmodel.dart';
import 'package:naemansan/models/trailcommentmodel.dart';

class TrailApiService {
  final String baseUrl = 'https://ossp.dcs-hyungjoon.com';

/*           TOKEN           */
  Future<Map<String, String?>> getTokens() async {
    const storage = FlutterSecureStorage();
    final accessToken = await storage.read(key: 'accessToken');
    final refreshToken = await storage.read(key: 'refreshToken');
    return {
      'accessToken': accessToken,
      'refreshToken': refreshToken,
    };
  }

/*           GET           */
  Future<http.Response> getRequest(String endpoint) async {
    try {
      final tokens = await getTokens();
      final accessToken = tokens['accessToken'];
      final refreshToken = tokens['refreshToken'];

      final response = await http.get(
        Uri.parse('$baseUrl/$endpoint'),
        headers: {
          'Authorization': 'Bearer $accessToken',
        },
      );

      if (response.statusCode == 200) {
        print('GET 요청 성공');
      } else {
        print('GET 요청 실패 - 상태 코드: ${response.statusCode}');
      }

      return response;
    } catch (e) {
      print('GET 요청 실패 - $e');
      return http.Response('Error', 500);
    }
  }

/*유저정보 가져오기*/
  Future<Map<String, dynamic>?> getUserInfo() async {
    try {
      final response = await getRequest('user');
      if (response.statusCode == 200) {
        final parsedResponse = jsonDecode(response.body);
        print(parsedResponse['data']);
        return parsedResponse['data'];
      } else {
        print('유저 정보 가져오기 실패 - ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('유저 정보 가져오기 실패 - $e');
      return null;
    }
  }

  /* ---------------- 산책로 탭 ---------------- */
  // 산책로 tap 추천순 전체 산책로 조회
  Future<List<TraildetailModel>?> getRecommendedCourses(
      int page, int num) async {
    try {
      final response =
          await getRequest('course/list/recommend?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TraildetailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TraildetailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('추천순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 Tap 거리순 전체 산책로 조회
  Future<List<TraildetailModel>?> getNearestCourses(
      int page, int num, double latitude, double longitude) async {
    try {
      final response = await getRequest(
          'course/list/location?page=$page&num=$num&latitude=$latitude&longitude=$longitude');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TraildetailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TraildetailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('거리순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 Tap - 좋아요수 전체 산책로 조회
  Future<List<TraildetailModel>?> getMostLikedTrail(int page, int num) async {
    try {
      final response = await getRequest('course/list/like?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TraildetailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TraildetailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('좋아요순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 Tap - 이용자순 전체 산책로 조회
  Future<List<TraildetailModel>?> getMostUsedTrail(int page, int num) async {
    try {
      final response =
          await getRequest('course/list/using?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TraildetailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TraildetailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('사용자순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 탭 - 등록된 전체 산책 코스 (최신순) 사용
  Future<List<TraildetailModel>?> getAllCourses(int page, int num) async {
    try {
      final response = await getRequest('course/list/all?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TraildetailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TraildetailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('등록된 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

/* ---------------- TAP 나만의 산책로 가져오기 ----------------course/list/individual/basic?page=$page&num=$num */

  // 나만의 Tap 등록한 산책로 조회 course/list/individual/enrollment?page=$page&num=$num
  Future<List<TrailModel>?> getEnrolledCourses(int page, int num) async {
    try {
      final response =
          await getRequest('course/list/individual/basic?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TrailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('등록한 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 나만의 Tap 좋아요한 산책로 조회 course/list/individual/like?page=$page&num=$num
  Future<List<TrailModel>?> getLikedCourses(int page, int num) async {
    try {
      final response =
          await getRequest('course/list/individual/like?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TrailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('좋아요한 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 나만의 Tap 이용한 산책로 조회 course/list/individual/using?page=$page&num=$num
  Future<List<TrailModel>?> getUsedCourses(int page, int num) async {
    try {
      final response =
          await getRequest('course/list/individual/using?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TrailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('좋아요한 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  //사용자 댓글 조회 /user/comment?page={}&num={} //여기 get 요청 실패 ...
  Future<List<TrailCommentModel>?> getCommentedCourses(
      //위젯 디자인이 달라 여기만 다른 위젯 사용
      int page,
      int num) async {
    try {
      final response = await getRequest('user/comment?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse = jsonDecode(response.body);
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TrailCommentModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TrailCommentModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('댓글 단 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 나만의 Tap Tag 기준 산책로 조회 course/list/individual/tag?page=$page&num=$num&name=$encodedTagName
  Future<List<TrailModel>?> getKeywordCourse(
      int page, int num, String tagName) async {
    try {
      final encodedTagName = Uri.encodeComponent(tagName);
      final response = await getRequest(
          'course/list/individual/tag?page=$page&num=$num&name=$encodedTagName');

      if (response.statusCode == 200) {
        final parsedResponse = jsonDecode(response.body);
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TrailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('키워드별 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 나만의 Tag Tag기준 산책로 조회 - tag 조회

  /*Future<List<TrailModel>?> getKeywordCourse(
      int page, int num, String tagName) async {
    try {
      final response =
          await getRequest('course/{courseId}/comment?page=$page&num=$num');

      if (response.statusCode == 200) {
        final parsedResponse =
            jsonDecode(response.body) as Map<String, dynamic>;
        final trails = parsedResponse['data'] as List<dynamic>;
        List<TrailModel> courseInstances = [];
        for (var trail in trails) {
          final instance = TrailModel.fromJson(trail);
          courseInstances.add(instance);
        }
        return courseInstances;
      } else {
        print('키워드별 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    } */
}
