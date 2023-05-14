import 'package:flutter/material.dart';

class CourseDetail extends StatelessWidget {
  final int id;
  final String title;

  const CourseDetail({
    super.key,
    required this.id,
    required this.title,
  });

  @override
  Widget build(BuildContext context) {
    // CourseDetail 페이지의 UI를 구현하고 반환하는 코드 작성

    return Scaffold(
      appBar: AppBar(
        title: Text(title),
      ),
      body: Center(
        child: Text('Course Detail Page - ID: $id, Title: $title'),
      ),
    );
  }
}
