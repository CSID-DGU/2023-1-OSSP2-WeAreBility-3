import 'package:flutter/material.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:naemansan/providers/location.dart';
import 'package:naemansan/screens/Home/banner_detail_screen.dart';
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
            home: isLogged
                ? const IndexScreen(index: 0)
                : const BannerDetailScreen(
                    caption: "내가 만든 산책로, 내만산이란?",
                    content: """
안녕하세요!\n
\n
내가 만드는 산책로, 내만산입니다.\n
\n
내만산은 양방향 산책로 공유 서비스 지원함으로서 ㄱ\n
개인만의 산책로를 알릴 수 있는 플랫폼입니다.\n
\n
[title]나만의 산책로 생성\n
\n
나만의 산책로를 만들어봐요!\n
나만의 산책로 -> 상단 + 아이콘을 클릭하면 나만의 산책로를 만들 수 있답니다.\n
직접 산책해야 길을 생성할 수 있습니다!\n
\n
[title]모두의 산책로\n
\n
나만의 산책로를 공유해봐요!\n
나만의 산책로를 공유함으로서 나만의 산책로를 더 널리알릴 수 있고 \n
다른이의 산책로를 이용함으로서 다양한 산책로들을 알아갈 수 있어요! 
\n
[title]맞춤형 산책로 추천\n
\n
공개등록된 산책로를 위치, 키워드 별로 추천받을 수 있습니다.\n
이를 통해 더 다양한 산책로들을 구경하고 이용하는\n
시간을 가져봐요 :)\n""",
                  ),
            routes: {
              '/index': (context) => const IndexScreen(index: 0),
              '/login': (context) => const LoginScreen(),
              "/createTitle": (context) => const CreateTitleScreen(),
              "/mytab": (context) => const IndexScreen(index: 2),
            },
          );
        }
      },
    );
  }
}
