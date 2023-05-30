import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:naemansan/widgets/login_button.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  dynamic userInfo = ''; // storage에 있는 유저 정보를 저장
  final GoogleSignIn googleSignIn = GoogleSignIn();
  static const storage =
      FlutterSecureStorage(); // FlutterSecureStorage를 storage로 저장

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    //비동기로 flutter secure storage 정보를 불러오는 작업.

    WidgetsBinding.instance.addPostFrameCallback((_) {
      _asyncMethod();
    });
  }

  _asyncMethod() async {
    //read 함수를 통하여 key값에 맞는 정보를 불러오게 됩니다. 이때 불러오는 결과의 타입은 String 타입임을 기억해야 합니다.
    //(데이터가 없을때는 null을 반환을 합니다.)
    userInfo = await storage.read(key: "login");
    print(userInfo);

    //user의 정보가 있다면 바로 로그아웃 페이지로 넝어가게 합니다.
    // user의 정보가 있다면 로그인 후 들어가는 첫 페이지로 넘어가게 합니다.
    if (userInfo != null) {
      Navigator.pushNamedAndRemoveUntil(context, '/index', (route) => false);
      // Navigator.pushNamedAndRemoveUntil(context, '/index', (route) => false);
    } else {
      print('로그인이 필요합니다');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      // 배경 이미지
      decoration: const BoxDecoration(
        image: DecorationImage(
          fit: BoxFit.cover,
          image: AssetImage('assets/images/login_screen.png'), // 배경 이미지
        ),
      ),
      child: Scaffold(
        backgroundColor: Colors.transparent, // 배경색을 투명으로 설정
        body: Center(
            child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const SizedBox(height: 60),
            LoginBtn(
              whatsLogin: "Kakao 계정으로 로그인",
              logo: "kakao",
              routeContext: context,
            ),
            const SizedBox(height: 20),
            LoginBtn(
              whatsLogin: "Apple 계정으로 로그인",
              logo: "apple",
              routeContext: context,
            ),
            const SizedBox(height: 20),
            LoginBtn(
              whatsLogin: "Google 계정으로 로그인",
              logo: "google",
              routeContext: context,
            ),
          ],
        )),
      ),
    );
  }
}
