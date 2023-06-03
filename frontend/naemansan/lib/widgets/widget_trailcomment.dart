import 'package:flutter/material.dart';
import 'dart:math';

//좌측에 아이콘, 우측에 댓글 내용만을 표시
class CommentTrailWidget extends StatelessWidget {
  final IconData icon;
  final String content;

  const CommentTrailWidget({
    Key? key,
    required this.icon,
    required this.content,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final random = Random();

    final iconList = [
      Icons.nature,
      Icons.nature_outlined,
      Icons.nature_people_outlined,
      Icons.nature_people_sharp,
      Icons.directions_walk_rounded,
      Icons.run_circle_outlined,
      Icons.art_track_outlined,
    ];

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
      child: Container(
        decoration: BoxDecoration(
          border: Border.all(
            color: Colors.grey.withOpacity(0.5),
            width: 1,
          ),
          borderRadius: BorderRadius.circular(15),
          color: Colors.white,
        ),
        height: 100.0,
        child: Row(
          children: [
            SizedBox(
              width: 100.0,
              child: Icon(iconList[random.nextInt(iconList.length)]),
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  content,
                  style: const TextStyle(fontSize: 16.0),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
