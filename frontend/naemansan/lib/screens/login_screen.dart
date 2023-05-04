import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:naemansan/widgets/login_button.dart';

class LoginScreen extends StatelessWidget {
  final GoogleSignIn googleSignIn = GoogleSignIn();

  LoginScreen({super.key});

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
