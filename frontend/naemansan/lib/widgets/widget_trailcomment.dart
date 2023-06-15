import 'package:flutter/material.dart';
import 'package:naemansan/screens/course_detail_byID.dart';

class CommentTrailWidget extends StatelessWidget {
  final int id;
  final String content;
  final String title;
  final int courseId;
  final List<String> tags;

  const CommentTrailWidget({
    Key? key,
    required this.id,
    required this.content,
    required this.title,
    required this.courseId,
    required this.tags,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 0.0, vertical: 0.0),
      child: Container(
        decoration: BoxDecoration(
          border: Border.all(
            color: Colors.grey.withOpacity(0.5),
            width: 1,
          ),
          borderRadius: BorderRadius.circular(15),
          color: Colors.white,
        ),
        height: 140.0,
        child: Row(
          children: [
            SizedBox(
              width: 255.0,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    title,
                    style: const TextStyle(
                      fontSize: 16.0,
                      fontWeight: FontWeight.bold,
                      color: Colors.black,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 4.0),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      for (var tag in tags)
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 2.0),
                          child: Text(
                            '#$tag',
                            style: const TextStyle(
                              fontSize: 12.0,
                              color: Colors.black,
                            ),
                          ),
                        ),
                    ],
                  ),
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          content,
                          style: const TextStyle(fontSize: 16.0),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            IconButton(
              icon: const Icon(Icons.arrow_forward_ios_outlined),
              onPressed: () {
                print('id: $id');
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    //-------------------------------------------------------------------------------------------------------------
                    builder: (context) => CourseDetailbyID(
                      id: id,
                      // 산책로 세부 페이지로 이동 -> ID 값 전달
                    ),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
