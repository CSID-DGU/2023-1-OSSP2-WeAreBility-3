import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_mypage.dart';

//홈,산책로, 나만의 산책로, 마이페이지 상단바에 사용된 구조를 변형해 사용
class Edit extends StatelessWidget {
  const Edit({super.key});

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
                    '프로필 수정',
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
            InkWell(
              onTap: () {
                // 버튼을 클릭 시 프로필 수정 완료
              },
              child: const Text(
                '완료',
                style: TextStyle(
                    color: Colors.black,
                    fontSize: 20.0,
                    fontWeight: FontWeight.w600),
              ),
            ),
            const SizedBox(width: 6),
          ],
        ),
      ),
    );
  }
}
