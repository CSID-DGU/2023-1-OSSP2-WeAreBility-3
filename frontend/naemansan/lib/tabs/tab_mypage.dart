// Mypage()
import 'package:flutter/material.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Mypage extends StatelessWidget {
  const Mypage({super.key});

  signOut(BuildContext context) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      prefs.setBool('isLogged', false);
      Navigator.pushNamedAndRemoveUntil(context, '/', (route) => false);

      await UserApi.instance.logout();
    } catch (error) {
      print('카카오계정으로 로그인 아웃 실패 $error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          ElevatedButton(
            onPressed: () => signOut(context),
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all(Colors.green),
            ),
            child: const Text('로그아웃'),
          ),
        ],
      ),
    );
  }
}
