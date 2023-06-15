import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/services/login_api_service.dart';

class CommentEditpage extends StatefulWidget {
  final int id;
  final String content;
  final int user_id;
  final int course_id;

  const CommentEditpage({
    Key? key,
    required this.id, //댓글의 아이디
    required this.content,
    required this.user_id, //사용자의 아이디
    required this.course_id, // 산책로 아이디
  }) : super(key: key);

  @override
  State<CommentEditpage> createState() => _CommentEditpageState();
}

class _CommentEditpageState extends State<CommentEditpage> {
  static const storage = FlutterSecureStorage();

  String newComment = '';

  // 산책로 정보 업데이트
  Future<void> saveChanges() async {
    final apiService = ApiService();
    // /course/{courseId}/comment/{commentId}
    final response =
        await apiService.changeComment(widget.course_id, widget.id, newComment);

    // 산책로 정보 다시 불러오기
  }

  @override
  void initState() {
    super.initState();
    newComment = widget.content;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        titleSpacing: 0,
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back_ios_outlined,
            color: Colors.black,
          ),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        title: Row(
          children: [
            const Padding(
              padding: EdgeInsets.only(left: 5.0),
              child: Text(
                '댓글 수정',
                style: TextStyle(
                  fontSize: 21,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const Spacer(),
            TextButton(
              onPressed: () {
                saveChanges();
                Navigator.of(context).pop(); // 이전 페이지로 이동
              },
              child: const Text(
                '저장',
                style: TextStyle(
                  fontSize: 16,
                  color: Colors.black,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
          ],
        ),
      ),
      body: ListView(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        children: [
          const SizedBox(height: 8.0),
          TextFormField(
            initialValue: widget.content,
            onChanged: (value) {
              setState(() {
                newComment = value;
              });
            },
            decoration: const InputDecoration(
              border: OutlineInputBorder(),
              hintText: '댓글 수정 중 ...',
            ),
          ),
        ],
      ),
    );
  }
}
