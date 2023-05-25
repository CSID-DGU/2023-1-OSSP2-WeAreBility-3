//로그인 권한 문제로 main.dart주석 처리 후 이전 버전으로 사용하였습니다//

import 'package:flutter/material.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:naemansan/screens/login_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';

// 알림

void main() async {
  // 환경변수
  await dotenv.load(fileName: 'assets/config/.env');
  // await dotenv.load(fileName: '.env'); c

  // spalsh 시간 조절하기
  WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
  FlutterNativeSplash.preserve(widgetsBinding: widgetsBinding);

  // prefs 초기화
  final prefs = await SharedPreferences.getInstance();
  // 로그인 여부 확인
  // final isLoggedin = prefs.getBool('isLoggedIn') ?? false;
  KakaoSdk.init(nativeAppKey: "${dotenv.env['YOUR_NATIVE_APP_KEY']}");

  runApp(const App());
}

//---
class App extends StatefulWidget {
  const App({super.key});

  @override
  _AppState createState() => _AppState();
}

class _AppState extends State<App> {
  bool isLogged_local = false;

  @override
  void initState() {
    super.initState();
    _checkLoginStatus();
    // 새로고침하면 로그인 상태가 반영이 안됨
    print("지?금 main.dart가 파악하는 로그인 상태는$isLogged_local");
  }

  Future<void> _checkLoginStatus() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      isLogged_local = prefs.getBool('isLogged') ?? false;
    });
  }

  Future<bool> isUserLoggedIn() async {
    const storage = FlutterSecureStorage();
    String? accessToken = await storage.read(key: 'accessToken');
    String? refreshToken = await storage.read(key: 'refreshToken');
    return accessToken != null && refreshToken != null;
  }

  @override
  Widget build(BuildContext context) {
    FlutterNativeSplash.remove(); // 초기화가 끝나는 시점에 삽입
    return FutureBuilder<bool>(
      future: isUserLoggedIn(),
      builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          // 데이터 로딩 중인 경우 표시할 위젯 (예: 로딩 스피너)
          return const CircularProgressIndicator();
        } else if (snapshot.hasError) {
          // 오류 발생 시 처리할 위젯 (예: 오류 메시지 표시)
          return Text('Error: ${snapshot.error}');
        } else {
          // 데이터가 정상적으로 로드된 경우 조건에 따라 페이지 이동
          return Directionality(
            textDirection: TextDirection.ltr, // 텍스트 방향 설정
            child: MaterialApp(
              title: '내가 만든 산책로',
              home: snapshot.data! ? const IndexScreen() : LoginScreen(),
              routes: {
                '/index': (context) => const IndexScreen(),
              },
            ),
          );
        }
      },
    );
  }
}
