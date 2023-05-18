import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginBtn extends StatelessWidget {
  final String whatsLogin;
  final String logo;
  final BuildContext routeContext;

  const LoginBtn(
      {super.key,
      required this.whatsLogin,
      required this.logo,
      required this.routeContext});

// 서버에 토큰 보내주고 user profile가져오기
  Future<void> fetchUserProfile(String accessToken) async {
    try {
      // backend server url
      const url = 'https://your-server-url.com/profile';

      // response 받아오기
      final response = await http.post(
        Uri.parse(url),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $accessToken',
        },
      );

      if (response.statusCode == 200) {
        // Profile fetched successfully
        final userProfile = jsonDecode(response.body);
        print('User Profile: $userProfile');
        // TODO: Handle the user profile data as per your requirement
      } else {
        // Error fetching profile
        print('Error fetching user profile: ${response.statusCode}');
      }
    } catch (error) {
      print('Error: $error');
    }
  }

// 로그인 유지
  Future<void> persistLogin() async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setBool('isLogged', true);
    // print(prefs.getBool('isLogged'));
  }

// Function to check if the user is logged in
  Future<bool> isLoggedIn() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool('isLogged') ?? false;
  }

  login() async {
    if (await isKakaoTalkInstalled()) {
      try {
        // flutter SDK를 사용하는 방식
        await UserApi.instance.loginWithKakaoTalk();

        // redirect 방식
        // redirectUri로 인가코드 발송
        // await AuthCodeClient.instance.authorizeWithTalk(
        //   redirectUri: 'http://localhost:8080/login/oauth2/code/kakao',
        // );
        // print('카카오톡으로 로그인 성공');
      } catch (error) {
        print('카카오톡으로 로그인 실패 $error');

        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
        if (error is PlatformException && error.code == 'CANCELED') {
          return;
        }
        // 카카오톡에 연결된 카카오계정이 없는 경우, web 카카오계정으로 로그인
        try {
          // // flutter SDK를 사용하는 방식
          // flutter SDK가 accesstoken, Refrest Token 발급 해줌.
          await UserApi.instance.loginWithKakaoAccount();

          // await AuthCodeClient.instance.authorize(
          //   redirectUri: 'http://localhost:8080/login/oauth2/code/kakao',
          // );
          // print('카카오계정으로 로그인 성공');
        } catch (error) {
          // print('카카오계정으로 로그인 실패 $error');
        }
      }
    } else {
      try {
        isKakaoTalkInstalled();

        // await AuthCodeClient.instance.authorize(
        //   redirectUri: 'http://localhost:8080/login/oauth2/code/kakao',
        // );

        OAuthToken token = await UserApi.instance.loginWithKakaoAccount();
        print('카카오계정으로 로그인 성공');
        print("ACCESS Token : ${token.accessToken}");
        print("REFRESH Token : ${token.refreshToken}");

        // 서비스 서버가 전달한 response 데이터에서 토큰 획득 후 Flutter SDK에서 사용하는 타입으로 변환
        // var tokenResponse = AccessTokenResponse.fromJson(response);
        // var token = OAuthToken.fromResponse(tokenResponse);

        // // 토큰 저장
        // TokenManagerProvider.instance.manager.setToken(token);

        // // 로그인 성공 시 isLogged 값을 true로 설정하여 SharedPreferences에 저장
        // final prefs = await SharedPreferences.getInstance();

        // prefs.setBool('isLogged', true);

        // Send the access token to the server and fetch the user profile
        await fetchUserProfile(token.accessToken);

        // Persist the login status
        await persistLogin();

        final navigator = Navigator.of(routeContext);
        navigator.pushNamed('/index');
      } catch (error) {
        // print('카카오계정으로 로그인 실패 $error');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        backgroundColor: const Color(0xFFF5F5F5),
        fixedSize: const Size(307, 50), // 버튼 크기
      ),
      onPressed: login,
      // 로고와 텍스트를 가로로 나열
      child: Row(
        // 로고와 텍스트를 가운데 정렬
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Image.asset(
            'assets/images/logo/$logo.png',
            width: 18,
          ),
          const SizedBox(width: 10),
          Text(
            whatsLogin,
            style: const TextStyle(
              fontSize: 15,
              color: Color(0xFF49454F),
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }
}
