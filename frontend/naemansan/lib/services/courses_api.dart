//산책로 탭, 나만의 산책로 탭에서 산책로 목록을 불러올 때 사용
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'package:naemansan/models/trailmodel.dart';

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

  /* ---------------- 산책로 탭 ---------------- */
  // 산책로 tap 추천순 전체 산책로 조회
  Future<List<TrailModel>?> getRecommendedCourses(int page, int num) async {
    try {
      final response =
          await getRequest('/course/list/recommend?page=$page&num=$num');

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
        print('추천순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 Tap 거리순 전체 산책로 조회
  Future<List<TrailModel>?> getNearestCourses(
      int page, int num, double latitude, double longitude) async {
    try {
      final response = await getRequest(
          'course/list/location?page=$page&num=$num&latitude=$latitude&longitude=$longitude');

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
        print('거리순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 Tap - 좋아요수 전체 산책로 조회
  Future<List<TrailModel>?> getMostLikedTrail(int page, int num) async {
    try {
      final response = await getRequest('course/list/like?page=$page&num=$num');

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
        print('좋아요순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 Tap - 이용자순 전체 산책로 조회
  Future<List<TrailModel>?> getMostUsedTrail(int page, int num) async {
    try {
      final response =
          await getRequest('course/list/using?page=$page&num=$num');

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
        print('사용자순 전체 산책로 조회 GET 요청 실패 - 상태 코드: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('실패 - $e');
      return null;
    }
  }

  // 산책로 탭 - 등록된 전체 산책 코스 (최신순) 사용 //조회 실패
  Future<List<TrailModel>?> getAllCourses(int page, int num) async {
    try {
      final response =
          await getRequest('course/list/all?page=$page&num=$num'); //재확인햐

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

  //댓글

  // 나만의 Tap Tag 기준 산책로 조회 course/list/individual/tag?page=$page&num=$num&name=$encodedTagName
  Future<List<TrailModel>?> getIndividualCoursesByTag(
      int page, int num, String tagName) async {
    final encodedTagName = Uri.encodeComponent(tagName);
    final response = await getRequest(
        'course/list/individual/tag?page=$page&num=$num&name=$encodedTagName');
    if (response.statusCode == 200) {
      final parsedResponse = jsonDecode(response.body);
      return parsedResponse['data'];
    } else {
      return null;
    }
  }
}
