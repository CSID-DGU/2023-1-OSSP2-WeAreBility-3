import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_mypage.dart';

//홈,산책로, 나만의 산책로, 마이페이지 상단바에 사용된 구조를 변형해 사용
class Badges extends StatelessWidget {
  const Badges({super.key});

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
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                //arrow 아이콘 클릭 시 마이페이지 화면으로 이동
                false;
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const Mypage(),
                  ),
                );
              },
            ),
            const Padding(
              padding: EdgeInsets.only(left: 5.0),
              child: Row(
                children: [
                  Text(
                    '뱃지',
                    style: TextStyle(
                      fontSize: 21,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  SizedBox(width: 8),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
