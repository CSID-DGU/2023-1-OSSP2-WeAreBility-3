import 'package:flutter/material.dart';

class CommentWidget extends StatelessWidget {
  final String content;

  const CommentWidget({Key? key, required this.content}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Text(
            content,
            style: const TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        IconButton(
          icon: const Icon(Icons.more_vert),
          onPressed: () {
            // 댓글 삭제 로직 추가
          },
        ),
      ],
    );
  }
}
