import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:naemansan/screens/webview_kakao_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

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

  @override
  Widget build(BuildContext context) {
    void login() async {
      var response = await http.get(
        Uri.parse("http://ossp.dcs-hyungjoon.com/auth/kakao"),
      );
      var parsedResponse = jsonDecode(response.body);
      String loginUrl = parsedResponse['data']['url'];

      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) =>
              WebViewScreenKakao(loginUrl: loginUrl), // Pass the loginUrl value
        ),
      );
    }

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
