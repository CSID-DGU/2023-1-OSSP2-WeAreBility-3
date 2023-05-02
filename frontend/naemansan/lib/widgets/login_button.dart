import 'package:flutter/material.dart';

class LoginBtn extends StatelessWidget {
  final String whatsLogin;

  const LoginBtn({super.key, required this.whatsLogin});

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        backgroundColor: const Color(0xFFF5F5F5),
        fixedSize: const Size(307, 50), // 버튼 크기
      ),
      onPressed: () {},
      child: Text(
        whatsLogin,
        style: const TextStyle(
          fontSize: 15,
          color: Color(0xFF49454F),
          fontWeight: FontWeight.w500,
        ),
      ),
    );
  }
}
