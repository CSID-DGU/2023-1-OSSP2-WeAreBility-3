import 'package:flutter/material.dart';

class ProfileIntroEditPage extends StatefulWidget {
  final Map<String, dynamic>? userInfo;

  const ProfileIntroEditPage({Key? key, required this.userInfo})
      : super(key: key);

  @override
  _ProfileIntroEditPageState createState() => _ProfileIntroEditPageState();
}

class _ProfileIntroEditPageState extends State<ProfileIntroEditPage> {
  late Future<Map<String, dynamic>?> user;
  String newIntro = '';

  @override
  void initState() {
    super.initState();
    newIntro = widget.userInfo?['introduction'] ?? '';
  }

  /* 이 탭에서 소개글을 직접 저장하지 않도록 수정함함
  Future<void> saveIntroChanges() async {
    // Put 요청 보내기
    final profileApiService = ProfileApiService();
    final response = await profileApiService.putRequest('user', {
      'introduction': newIntro,
    });

    if (response.statusCode == 200) {
      print('프로필 introduction 수정 성공');
      // 프로필 수정 완료 후 다른 작업 수행
    } else {
      print('프로필 introduction 수정 실패 - 상태 코드: ${response.statusCode}');
      // 실패 시 에러 처리
    }
  }
  */
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        backgroundColor: Colors.white,
        elevation: 2,
        title: const Text(
          '프로필 수정',
          style: TextStyle(
            fontSize: 21,
            fontWeight: FontWeight.w600,
            color: Colors.black,
          ),
        ),
        iconTheme: const IconThemeData(color: Colors.black),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const SizedBox(height: 20),
            Container(
              margin: const EdgeInsets.symmetric(horizontal: 20),
              child: TextField(
                decoration: const InputDecoration(
                  hintText: '소개 메시지를 입력하세요',
                ),
                onChanged: (value) {
                  setState(() {
                    newIntro = value;
                  });
                },
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                //saveIntroChanges();
                setState(() {
                  {
                    widget.userInfo!['introduction'] = newIntro;
                  }
                });

                Navigator.of(context).pop(newIntro);
              },
              style: ElevatedButton.styleFrom(
                foregroundColor: Colors.black,
                backgroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(
                  vertical: 10,
                  horizontal: 20,
                ),
                side: const BorderSide(color: Colors.black),
              ),
              child: const Text(
                '완료',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
