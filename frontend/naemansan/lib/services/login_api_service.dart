import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class ApiService {
  final String baseUrl = 'https://ossp.dcs-hyungjoon.com';

  Future<Map<String, String?>> getTokens() async {
    const storage = FlutterSecureStorage();
    final accessToken = await storage.read(key: 'accessToken');
    final refreshToken = await storage.read(key: 'refreshToken');
    return {
      'accessToken': accessToken,
      'refreshToken': refreshToken,
    };
  }

// get 요청
// get 요청
  Future<http.Response> getRequest(String endpoint) async {
    try {
      final tokens = await getTokens();
      final accessToken = tokens['accessToken'];
      final refreshToken = tokens['refreshToken'];

      print('Access Token: $accessToken');
      print('Refresh Token: $refreshToken');

      final response = await http.get(
        Uri.parse('$baseUrl/$endpoint'),
        headers: {
          'Authorization': 'Bearer $accessToken',
        },
      );
      print('$baseUrl/$endpoint');

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

// post 요청
  Future<http.Response> postRequest(String endpoint, dynamic body) async {
    try {
      final accessToken = await getTokens();
      final response = await http.post(
        Uri.parse('$baseUrl/$endpoint'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'Content-Type': 'application/json',
        },
        body: jsonEncode(body),
      );
      return response;
    } catch (e) {
      print('POST 요청 실패 - $e');
      return http.Response('Error', 500);
    }
  }

  // 예시 API 메서드: 유저 정보 가져오기
  // 예시 API 메서드: 유저 정보 가져오기
  Future<Map<String, dynamic>?> getUserInfo() async {
    try {
      final response = await getRequest('user');
      if (response.statusCode == 200) {
        final parsedResponse = jsonDecode(response.body);
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

  // 예시 API 메서드: 산책로 정보 가져오기
  Future<List<dynamic>?> getCourseList() async {
    final response = await getRequest('course');
    if (response.statusCode == 200) {
      final parsedResponse = jsonDecode(response.body);
      return parsedResponse['data'];
    } else {
      return null;
    }
  }
}
