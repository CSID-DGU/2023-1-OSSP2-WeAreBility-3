import 'package:flutter/material.dart';

class CourseDetail extends StatefulWidget {
  final int id;
  final String title;

  const CourseDetail({
    Key? key,
    required this.id,
    required this.title,
  }) : super(key: key);

  @override
  _CourseDetailState createState() => _CourseDetailState();
}

class _CourseDetailState extends State<CourseDetail> {
  int likes = 0;
  List<String> comments = [];

  void addComment(String comment) {
    setState(() {
      comments.add(comment);
    });
  }

  void likeCourse() {
    setState(() {
      likes++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        title: Text(widget.title),
        actions: [
          IconButton(
            icon: const Icon(Icons.more_vert),
            onPressed: () {
              // Handle URL sharing functionality
            },
          ),
        ],
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Course Detail',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 16),
            Text(
              'ID: ${widget.id}',
              style: const TextStyle(
                fontSize: 16,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              'Title: ${widget.title}',
              style: const TextStyle(
                fontSize: 16,
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              'Description:',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              '산책로 내용물',
              style: TextStyle(
                fontSize: 16,
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              'Instructor:',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              '산책로 작성자',
              style: TextStyle(
                fontSize: 16,
              ),
            ),
            const SizedBox(height: 24),
            const Text(
              '댓글:',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            Expanded(
              child: ListView.builder(
                itemCount: comments.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(comments[index]),
                  );
                },
              ),
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                IconButton(
                  icon: const Icon(Icons.favorite_border),
                  onPressed: likeCourse,
                ),
                Text(
                  likes.toString(),
                  style: const TextStyle(
                    fontSize: 16,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            TextField(
              decoration: InputDecoration(
                labelText: 'Add a comment',
                suffixIcon: IconButton(
                  icon: const Icon(Icons.send),
                  onPressed: () {
                    addComment('New comment');
                  },
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
