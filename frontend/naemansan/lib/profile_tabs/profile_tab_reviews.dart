import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_mypage.dart';

// 탭 연결로 바꿔서 이 파일 필요 없음

class Reviews extends StatelessWidget {
  const Reviews({super.key});

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
                //arrow 아이콘 클릭 시 마이페이지 화면으로 이동
                false;
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const Mypage(),
                  ),
                );
              },
            ),
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: const [
                  Text(
                    '후기',
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
