import 'package:flutter/material.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:naemansan/providers/location.dart';
import 'package:naemansan/screens/login_screen.dart';
import 'package:naemansan/screens/map/create_title_map.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:provider/provider.dart';
import 'package:flutter/services.dart';
// firebase
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

void main() async {
  // 환경변수
  await dotenv.load(fileName: 'assets/config/.env');
  // await dotenv.load(fileName: '.env'); c

// firebase
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  // spalsh 시간 조절하기
  WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
  FlutterNativeSplash.preserve(widgetsBinding: widgetsBinding);
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
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
    if (isLogged == false) {
      // goLogin();
    }
    setState(
      () {},
    );
  }

  goLogin() {
    // Navigator.pushNamedAndRemoveUntil(context, '/login', (route) => false);
  }

  Future<bool> isUserLoggedIn() async {
    const storage = FlutterSecureStorage();
    String? accessToken = await storage.read(key: 'accessToken');
    String? refreshToken = await storage.read(key: 'refreshToken');
    if (accessToken == null) {
      goLogin();
    }
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
              "/createTitle": (context) => const CreateTitleScreen(),
            },
          );
        }
      },
    );
  }
}
