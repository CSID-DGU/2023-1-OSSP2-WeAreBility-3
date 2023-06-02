import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  WebViewController? _webViewController;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Login'),
      ),
      body: WebView(
        initialUrl:
            'https://kauth.kakao.com/oauth/authorize?client_id=db9bfe01c95414e0f1add469fdfaa9ff&redirect_uri=http://34.22.79.208:8080/auth/kakao/callback&response_type=code',
        javascriptMode: JavascriptMode.unrestricted,
        onWebViewCreated: (WebViewController webViewController) {
          _webViewController = webViewController;
        },
        navigationDelegate: (NavigationRequest request) async {
          if (request.url
              .startsWith('http://34.22.79.208:8080/auth/kakao/callback')) {
            // 콜백 URL에 도달하면 토큰 처리를 수행
            String code = Uri.parse(request.url).queryParameters['code'] ?? '';

            final url = Uri.parse(request.url);
            // 토큰 요청
            var response = await http.get(
              Uri.parse(
                  "http://34.22.79.208:8080/auth/kakao/callback?code=$code"),
            );
            var parseResponse = jsonDecode(response.body);
            // print(parseResponse);

            if (response.statusCode == 200) {
              // API 응답 값
              String accessToken = parseResponse['data']['jwt']['accessToken'];
              String refreshToken =
                  parseResponse['data']['jwt']['refreshToken'];

              // 저장
              await saveTokens(accessToken, refreshToken);

              // 로그인 성공 후 다음 동작 수행
              // 예를 들어, 다음 페이지로 이동하거나 홈 화면을 열 수 있습니다.

              // // WebView 종료
              // Navigator.pop(context);
            } else {
              // 로그인 실패 처리
              // 에러 메시지를 표시하거나 다시 시도하도록 안내할 수 있습니다.
            }

            return NavigationDecision.prevent;
          }

          return NavigationDecision.navigate;
        },
      ),
    );
  }

  Future<void> saveTokens(String accessToken, String refreshToken) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString('accessToken', accessToken);
    await prefs.setString('refreshToken', refreshToken);
  }
}
