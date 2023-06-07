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
  String? newIntro = '';

  @override
  void initState() {
    super.initState();
    newIntro = widget.userInfo?['introduction'] ?? '';
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
                setState(() {
                  {
                    widget.userInfo?['introduction'] = newIntro;
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
