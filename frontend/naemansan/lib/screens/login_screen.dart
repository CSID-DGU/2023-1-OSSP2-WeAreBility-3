import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';

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
      child: const Scaffold(
        backgroundColor: Colors.transparent, // 배경색을 투명으로 설정
      ),
    );
  }
}
