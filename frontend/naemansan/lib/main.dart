//로그인 권한 문제로 main.dart주석 처리 후 이전 버전으로 사용하였습니다//

import 'package:flutter/material.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:naemansan/providers/location.dart';
import 'package:naemansan/screens/login_screen.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:provider/provider.dart';

// 알림

void main() async {
  // 환경변수
  await dotenv.load(fileName: 'assets/config/.env');
  // await dotenv.load(fileName: '.env'); c

  // spalsh 시간 조절하기
  WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
  FlutterNativeSplash.preserve(widgetsBinding: widgetsBinding);

  // 로그인 여부 확인
  // final isLoggedin = prefs.getBool('isLoggedIn') ?? false;
  KakaoSdk.init(nativeAppKey: "${dotenv.env['YOUR_NATIVE_APP_KEY']}");

  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => LocationProvider(),
        ),
      ],
      child: const App(),
    ),
  );
}

//---
class App extends StatefulWidget {
  const App({super.key});

  @override
  _AppState createState() => _AppState();
}

class _AppState extends State<App> {
  bool isLogged = false;
  static const storage = FlutterSecureStorage();

  dynamic userInfo = '';

  @override
  void initState() {
    getLoginStatus();
    super.initState();
  }

  Future<void> getLoginStatus() async {
    userInfo = await storage.read(key: 'login');
    // print("userInfo 가 있냐고 $userInfo");
    userInfo == null ? isLogged = false : isLogged = true;

    setState(
      () {},
    );

    // 새로고침하면 로그인 상태가 반영이 안됨
    // print("🤔지금 main.dart가 파악하는 로그인 상태는$isLogged");
  }

  Future<bool> isUserLoggedIn() async {
    const storage = FlutterSecureStorage();
    String? accessToken = await storage.read(key: 'accessToken');
    String? refreshToken = await storage.read(key: 'refreshToken');
    return accessToken != null && refreshToken != null;
  }

  @override
  Widget build(BuildContext context) {
    FlutterNativeSplash.remove();
    return FutureBuilder<bool>(
      future: isUserLoggedIn(),
      builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const CircularProgressIndicator();
        } else if (snapshot.hasError) {
          return Text('Error: ${snapshot.error}');
        } else {
          return MaterialApp(
            title: '내가 만든 산책로',
            home: isLogged ? const IndexScreen() : const LoginScreen(),
            routes: {
              '/index': (context) => const IndexScreen(),
              '/login': (context) => const LoginScreen(),
            },
          );
        }
      },
    );
  }
}
