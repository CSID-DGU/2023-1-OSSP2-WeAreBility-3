import 'package:flutter/material.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Mypage extends StatelessWidget {
  const Mypage({super.key});

// 로그아웃
  logout(BuildContext context) async {
    try {
      // Clear the login status in SharedPreferences

      final prefs = await SharedPreferences.getInstance();
      prefs.setBool('isLogged', false);

      prefs.setBool('isLogged', false);
      print("로그아웃 성공");

      print(prefs.getBool('isLogged'));

      Navigator.pushReplacementNamed(context, '/');

      await UserApi.instance.logout();
    } catch (error) {
      print('카카오계정으로 로그인 아웃 실패 $error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        automaticallyImplyLeading: false,
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            IconButton(
              padding: EdgeInsets.zero,
              constraints: const BoxConstraints.tightFor(width: 20),
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const IndexScreen(),
                  ),
                );
              },
            ),
            const Padding(
              padding: EdgeInsets.only(left: 20),
              child: Text(
                '마이페이지',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const Expanded(child: SizedBox(width: 30)), // 여백 추가
            IconButton(
              padding: const EdgeInsets.only(left: 25),
              icon: const Icon(
                Icons.settings,
                color: Colors.black,
              ),
              onPressed: () {},
            ),
          ],
        ),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () => logout(context),
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(Colors.green),
              ),
              child: const Text('로그아웃'),
            ),
          ],
        ),
      ),
    );
  }
}
