import 'package:flutter/material.dart';
import 'package:naemansan/services/mypage_api_service.dart';

class ProfileNameEditPage extends StatefulWidget {
  final Map<String, dynamic>? userInfo;

  const ProfileNameEditPage({Key? key, required this.userInfo})
      : super(key: key);

  @override
  _ProfileNameEditPageState createState() => _ProfileNameEditPageState();
}

class _ProfileNameEditPageState extends State<ProfileNameEditPage> {
  late Future<Map<String, dynamic>?> user;
  String newName = '';

  @override
  void initState() {
    super.initState();
    newName = widget.userInfo?['name'] ?? '';
  }

  Future<void> saveNameChanges() async {
    // Put 요청 보내기
    final profileApiService = ProfileApiService();
    final response = await profileApiService.putRequest('user', {
      'name': newName,
    });

    if (response.statusCode == 200) {
      print('프로필 수정 성공');
      // 프로필 수정 완료 후 다른 작업 수행
    } else {
      print('프로필 수정 실패 - 상태 코드: ${response.statusCode}');
      // 실패 시 에러 처리
    }
  }

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
                  hintText: '이름을 입력하세요',
                ),
                onChanged: (value) {
                  setState(() {
                    newName = value;
                  });
                },
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                saveNameChanges();
                setState(() {
                  // 마이페이지 프로필 수정 페이지의 상태를 갱신하여 새로운 이름을 적용
                  if (widget.userInfo != null) {
                    widget.userInfo!['name'] = newName;
                  }
                });

                Navigator.of(context).pop(true);
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
