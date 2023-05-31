import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class CourseDetail extends StatefulWidget {
  final int id;
  final String title;
  final String location;
  final double length;
  final int likes;
  final List<String> keywords;
  final String created_date;

  const CourseDetail({
    Key? key,
    required this.id,
    required this.title,
    required this.location,
    required this.length,
    required this.likes,
    required this.keywords,
    required this.created_date,
  }) : super(key: key);

  @override
  _CourseDetailState createState() => _CourseDetailState();
}

class _CourseDetailState extends State<CourseDetail> {
  int likes = 0;
  List<String> comments = [];
  bool _isLiked = false;

  void addComment(String comment) {
    setState(() {
      comments.add(comment);
    });
  }

  void toggleLike() {
    setState(() {
      if (_isLiked) {
        likes--;
        _isLiked = false;
      } else {
        likes++;
        _isLiked = true;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final double lengthInKm = widget.length / 1000;
    final formattedDate =
        DateFormat("MM/dd").format(DateTime.parse(widget.created_date));

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
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.all(10.0),
                child: Row(
                  children: const [
                    CircleAvatar(
                      radius: 20,
                      backgroundImage: NetworkImage(
                        'https://avatars.githubusercontent.com/u/78739194?v=4',
                      ),
                    ),
                    SizedBox(width: 15),
                    Text("KAKAO-014107960443",
                        style: TextStyle(
                            fontSize: 17, fontWeight: FontWeight.w500)),
                  ],
                ),
              ),
              SizedBox(
                height: 300, // Adjust the height as needed
                child: Image.network(
                  'https://velog.velcdn.com/images/seochan99/post/41b2700b-2789-46a3-b232-011624a4cec3/image.png',
                  fit: BoxFit.cover,
                ),
              ),
              const SizedBox(height: 16),

              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    widget.title,
                    style: const TextStyle(
                      fontSize: 25,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  IconButton(
                    icon: Icon(
                      _isLiked ? Icons.favorite : Icons.favorite_border,
                      color: _isLiked ? Colors.red : null,
                    ),
                    onPressed: toggleLike,
                  ),
                ],
              ),
              Text(
                '생성날짜: $formattedDate',
                style: const TextStyle(
                  fontSize: 16,
                ),
              ),
              Text(
                '좋아요: $likes',
                style: const TextStyle(
                  fontSize: 16,
                ),
              ),
              const SizedBox(height: 8),

              const SizedBox(height: 16),
              Text(
                '시작위치: ${widget.location}',
                style: const TextStyle(
                  fontSize: 18,
                ),
              ),
              const SizedBox(height: 8),
              Text(
                '길이: ${lengthInKm.toStringAsFixed(2)} km',
                style: const TextStyle(
                  fontSize: 16,
                ),
              ),
              const SizedBox(height: 8),

              const SizedBox(height: 15),
              const Text(
                '🎯 키워드',
                style: TextStyle(
                  fontSize: 25,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 4,
                children: widget.keywords.map((keyword) {
                  return Chip(
                    label: Text(
                      keyword,
                      style: const TextStyle(
                        color: Colors.black87,
                      ),
                    ),
                    backgroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                      side: const BorderSide(
                        color: Colors.grey,
                        width: 1.0,
                      ),
                      borderRadius: BorderRadius.circular(4.0),
                    ),
                  );
                }).toList(),
              ),
              const SizedBox(height: 24),
              // Add your content here
              TextField(
                decoration: InputDecoration(
                  labelText: '댓글',
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
      ),
    );
  }
}
