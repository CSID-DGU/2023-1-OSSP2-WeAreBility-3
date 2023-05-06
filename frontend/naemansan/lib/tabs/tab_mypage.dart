import 'package:flutter/material.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:naemansan/screens/screen_index.dart';
<<<<<<< HEAD

/*
import 'package:naemansan/profile_tabs/badges.dart';
import 'package:naemansan/profile_tabs/my_reviews.dart';
import 'package:naemansan/profile_tabs/profile_edit.dart';
import 'package:naemansan/profile_tabs/settings.dart';
*/
=======
>>>>>>> 0b3aef621885e355994d06106904f7569b163854

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
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            IconButton(
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                //arrow 아이콘 클릭 시 홈 화면으로 이동 (홈 화면에 화살표 생김)
                false;
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const IndexScreen(),
                  ),
                );
              },
            ),
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: const [
                  Text(
                    '마이페이지',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  SizedBox(width: 8),
                ],
              ),
            ),
            const Spacer(),
            IconButton(
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
              onPressed: () => signOut(context),
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
