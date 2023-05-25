// Mypage()
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/screens/screen_index.dart';

class Mypage extends StatelessWidget {
  const Mypage({super.key});

// 로그아웃

  Future<void> deleteTokens() async {
    const storage = FlutterSecureStorage();
    await storage.delete(key: 'accessToken');
    await storage.delete(key: 'refreshToken');
  }

  @override
  Widget build(BuildContext context) {
    Future<void> logout() async {
      await deleteTokens();
      Navigator.pushNamedAndRemoveUntil(context, '/', (route) => false);
    }

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
              onPressed: () => logout(),
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
