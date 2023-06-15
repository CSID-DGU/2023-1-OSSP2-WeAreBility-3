import 'package:flutter/material.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/screens/comment_edit.dart';

// 산책로 디테일 페이지에서 댓글 볼 때 사용
class CommentWidget extends StatefulWidget {
  final String content;
  final int user_id;
  final int course_id;
  final int id;

  const CommentWidget(
      {Key? key,
      required this.id,
      required this.course_id,
      required this.content,
      required this.user_id})
      : super(key: key);
  @override
  _CommentWidgetState createState() => _CommentWidgetState();
}

class _CommentWidgetState extends State<CommentWidget> {
  @override
  void initState();

  //산책로 Delete
  Future<void> deleteComment() async {
    print("${widget.content} 삭제");
    ApiService apiService = ApiService();

    apiService.deleteComment(
        widget.course_id, widget.id); //course id, comment id
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Text(
            widget.content,
            style: const TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        //
        IconButton(
          icon: const Icon(Icons.more_vert),
          onPressed: () {
            // 댓글 삭제 로직 추가
            _showPopupMenu(context);
          },
        ),
      ],
    );
  }

  void _showPopupMenu(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              ListTile(
                title: const Text('수정'),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => CommentEditpage(
                        id: widget.id, //산책로 아이디
                        content: widget.content,
                        course_id: widget.course_id,
                      ),
                    ),
                  );
                  // 수정 페이지로 이동
                },
              ),
              ListTile(
                title: const Text('삭제'),
                onTap: () {
                  Navigator.of(context).pop();
                  _showDeleteConfirmationDialog(context);
                },
              ),
            ],
          ),
        );
      },
    );
  }

  //삭제
  void _showDeleteConfirmationDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('삭제 확인'),
          content: const Text('정말 삭제하시겠습니까?'),
          actions: [
            TextButton(
              child: const Text('취소'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: const Text('삭제'),
              onPressed: () {
                deleteComment();
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }
}
