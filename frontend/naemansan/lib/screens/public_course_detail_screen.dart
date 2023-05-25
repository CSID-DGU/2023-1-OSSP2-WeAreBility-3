import 'package:flutter/material.dart';

class PublicCourseDetailScreen extends StatefulWidget {
  final String courseName;

  const PublicCourseDetailScreen({Key? key, required this.courseName})
      : super(key: key);

  @override
  _PublicCourseDetailScreenState createState() =>
      _PublicCourseDetailScreenState();
}

class _PublicCourseDetailScreenState extends State<PublicCourseDetailScreen> {
  bool isUnderReview = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: const Text("나만의 산책로 공개하기"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            const Text(
              '🌿 공개하고 싶은 산책 코스 🌿',
              style: TextStyle(fontSize: 20),
              textAlign: TextAlign.center,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Row(
                  children: [
                    Text(
                      widget.courseName,
                      style: const TextStyle(fontSize: 20),
                    ),
                    isUnderReview
                        ? const Text(
                            '(심사 진행 중)',
                            style:
                                TextStyle(fontSize: 16, color: Colors.orange),
                          )
                        : const Text(
                            '(심사 진행 전)',
                            style:
                                TextStyle(fontSize: 16, color: Colors.orange),
                          ),
                  ],
                ),
                ElevatedButton(
                  onPressed: () {
                    setState(() {
                      isUnderReview = true;
                    });
                    showDialog(
                      context: context,
                      builder: (context) {
                        return AlertDialog(
                          title: const Text('알림'),
                          content: const Text('산책로 심사 접수 완료됐습니다.'),
                          actions: [
                            TextButton(
                              onPressed: () {
                                Navigator.pop(context);
                              },
                              child: const Text('확인'),
                            ),
                          ],
                        );
                      },
                    );
                  },
                  style: ElevatedButton.styleFrom(
                    foregroundColor: Colors.black,
                    backgroundColor: Colors.white,
                    side: const BorderSide(color: Colors.black),
                  ),
                  child: Text(isUnderReview ? '심사 중' : '공개하기'),
                ),
              ],
            ),
            const SizedBox(height: 16),
            const SizedBox(height: 16),
          ],
        ),
      ),
    );
  }
}
