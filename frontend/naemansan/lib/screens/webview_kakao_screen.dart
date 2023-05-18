import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:http/http.dart' as http;

class WebViewScreenKakao extends StatefulWidget {
  const WebViewScreenKakao({super.key});

  @override
  _WebViewScreenKakaoState createState() => _WebViewScreenKakaoState();
}

class _WebViewScreenKakaoState extends State<WebViewScreenKakao> {
  final flutterWebViewPlugin = FlutterWebviewPlugin();

  @override
  void initState() {
    super.initState();
    flutterWebViewPlugin.onUrlChanged.listen((String url) async {
      if (url.startsWith('http://34.22.79.208:8080/auth/kakao/callback')) {
        // 콜백 URL에 도달하면 토큰 처리를 수행
        String code = Uri.parse(url).queryParameters['code'] ?? '';

        // 토큰 요청
        var response = await http.get(
          Uri.parse("http://34.22.79.208:8080/auth/kakao/callback?code=$code"),
        );
        var parsedResponse = jsonDecode(response.body);
        print(parsedResponse);

        if (response.statusCode == 200) {
          // API 응답 값
          String accessToken = parsedResponse['data']['jwt']['accessToken'];
          String refreshToken = parsedResponse['data']['jwt']['refreshToken'];

          // 저장
          await saveTokens(accessToken, refreshToken);

          // 로그인 성공 후 다음 동작 수행
          // 예를 들어, 다음 페이지로 이동하거나 홈 화면을 열 수 있습니다.

          // WebView 종료
          Navigator.pushNamed(context, '/index');
          // Navigator.pop(context);
        } else {
          // 로그인 실패 처리
          // 에러 메시지를 표시하거나 다시 시도하도록 안내할 수 있습니다.
        }
      }
    });
  }

  @override
  void dispose() {
    flutterWebViewPlugin.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return WebviewScaffold(
      url:
          'https://kauth.kakao.com/oauth/authorize?client_id=db9bfe01c95414e0f1add469fdfaa9ff&redirect_uri=http://34.22.79.208:8080/auth/kakao/callback&response_type=code',
      appBar: AppBar(
        title: const Text('Login'),
      ),
    );
  }
}

Future<void> saveTokens(String accessToken, String refreshToken) async {
  const storage = FlutterSecureStorage();
  await storage.write(key: 'accessToken', value: accessToken);
  await storage.write(key: 'refreshToken', value: refreshToken);
}
