import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:io';

class ProfileApiService {
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

  /*           PUT           */
  Future<http.Response> putRequest(String endpoint, dynamic body) async {
    try {
      final tokens = await getTokens();
      final accessToken = tokens['accessToken'];
      final refreshToken = tokens['refreshToken'];

      final response = await http.put(
        Uri.parse('$baseUrl/$endpoint'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'Content-Type': 'application/json',
        },
        body: jsonEncode(body),
      );

      if (response.statusCode == 200) {
        print('PUT 요청 성공');
      } else {
        print('PUT 요청 실패 - 상태 코드: ${response.statusCode}');
      }

      return response;
    } catch (e) {
      print('PUT 요청 실패 - $e');
      return http.Response('Error', 500);
    }
  }

  /*           POST           */
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
      return http.Response('Error', 00);
    }
  }

// 본인 프로필 이미지 수정
  Future<http.Response> updateProfilePicture(File imageFile) async {
    try {
      final tokens = await getTokens();
      final accessToken = tokens['accessToken'];
      final uri = Uri.parse('https://ossp.dcs-hyungjoon.com/image/user');

      final request = http.MultipartRequest('POST', uri);
      request.headers['Authorization'] = 'Bearer $accessToken';

      final fileStream = http.ByteStream(imageFile.openRead());
      final fileLength = await imageFile.length();

      final multipartFile = http.MultipartFile(
        'image',
        fileStream,
        fileLength,
        filename: 'image.png', //jpg랑 png둘 다 받게 바꾸기
      );

      request.files.add(multipartFile);

      final response = await request.send();

      if (response.statusCode == 200) {
        print('프로필 사진 수정 POST 요청 성공');
      } else {
        print('프로필 사진 수정 POST 요청 실패 - 상태 코드: ${response.statusCode}');
      }

      return http.Response.fromStream(response);
    } catch (e) {
      print('프로필 사진 수정 POST 요청 실패 - $e');
      return http.Response('Error', 0);
    }
  }
}

// 프로필 : 이미지, 닉네임, 자기소개
// 본인 프로필 조회 (get) user
// 본인 프로필 수정 (put) user
// 본인 프로필 사진 수정 (post) image/user
// 본인 회원 탈퇴 (delete) user

// 팔로워 목록 조회 (get) user/follower?page={}&num={}
// 팔로잉 목록 조회 (get) user/following?page={}&num={}
