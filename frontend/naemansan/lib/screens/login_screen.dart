import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:naemansan/widgets/login_button.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  dynamic userInfo = ''; // storage에 있는 유저 정보를 저장
  final GoogleSignIn googleSignIn = GoogleSignIn();
  static const storage =
      FlutterSecureStorage(); // FlutterSecureStorage를 storage로 저장
  bool isLoading = true; // 로딩 상태를 나타내는 변수

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _asyncMethod();
    });
  }

  _asyncMethod() async {
    setState(() {
      isLoading = true; // 로딩 상태 갱신
    });

    userInfo = await storage.read(key: "login");
    print(userInfo);

    if (userInfo != null) {
      Navigator.pushNamedAndRemoveUntil(context, '/index', (route) => false);
    } else {
      print('로그인이 필요합니다');
    }

    setState(() {
      isLoading = false; // 로딩 상태 갱신
    });
  }

  Widget _buildLoginButtons() {
    return Column(
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
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
          fit: BoxFit.cover,
          image: AssetImage('assets/images/login_screen.png'),
        ),
      ),
      child: Scaffold(
        backgroundColor: Colors.transparent,
        body: Center(
          child: isLoading
              ? const CircularProgressIndicator() // 로딩 중에 표시될 위젯
              : _buildLoginButtons(), // 로딩이 완료된 후에 표시될 위젯
        ),
      ),
    );
  }
}
