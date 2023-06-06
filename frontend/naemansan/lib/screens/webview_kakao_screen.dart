import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:http/http.dart' as http;

class WebViewScreenKakao extends StatefulWidget {
  final String loginUrl;

  const WebViewScreenKakao({Key? key, required this.loginUrl})
      : super(key: key);

  @override
  _WebViewScreenKakaoState createState() => _WebViewScreenKakaoState();
}

class _WebViewScreenKakaoState extends State<WebViewScreenKakao> {
  final flutterWebViewPlugin = FlutterWebviewPlugin();

  @override
  void initState() {
    super.initState();
    flutterWebViewPlugin.onUrlChanged.listen((String url) async {
      // print(url);
      if (url
          .startsWith('https://ossp.dcs-hyungjoon.com/auth/kakao/callback')) {
        // Callback URL reached, process the token
        String code = Uri.parse(url).queryParameters['code'] ?? '';

        // Token request
        var response = await http.get(
          Uri.parse(
              "https://ossp.dcs-hyungjoon.com/auth/kakao/callback?code=$code"),
        );

        var parsedResponse = jsonDecode(response.body);
        // print(parsedResponse);

        if (response.statusCode == 200) {
          // API response value
          String accessToken = parsedResponse['data']['jwt']['accessToken'];
          String refreshToken = parsedResponse['data']['jwt']['refreshToken'];
          await saveTokens(accessToken, refreshToken);
          // Login successful, perfo rm next action
          Navigator.pushNamedAndRemoveUntil(
              context, '/index', (route) => false);
        } else {
          // Handle login failure
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
    if (widget.loginUrl == null) {
      // Display a loading indicator or handle the case where loginUrl is null
      return const CircularProgressIndicator(
        color: Colors.black,
      );
    }
    return WebviewScaffold(
      url: widget.loginUrl,
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
