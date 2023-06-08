import 'package:flutter/material.dart';

class Settings extends StatelessWidget {
  const Settings({super.key});

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
                Navigator.of(context).pop();
              },
            ),
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: const [
                  Text(
                    '설정',
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
      body: Padding(
        padding: const EdgeInsets.all(10.0),
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('알림 설정'),
                    // 그냥 여기 옆에 버튼 만들쟈
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    // 스토어 연결
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: const [
                        Text(
                          '버전 정보',
                          style: TextStyle(
                            color: Colors.black,
                            fontSize: 18,
                          ),
                        ),
                        Text(
                          '1.0.0', // 현재 버전 표시
                          style: TextStyle(
                            color: Colors.grey,
                            fontSize: 14,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('문의하기'), //고객센터 ? 이메일 ? 연결
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('로그아웃'), // 마이페이지에 있는거 여기로 옮겨
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.red,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('내만산 탈퇴하기'),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
