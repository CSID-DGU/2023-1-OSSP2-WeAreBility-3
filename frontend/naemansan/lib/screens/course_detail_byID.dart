import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'dart:convert';
import 'package:naemansan/models/traildetailmodel.dart';
import 'package:naemansan/services/courses_api.dart';

class CourseDetailbyID extends StatefulWidget {
  final int id;

  const CourseDetailbyID({
    Key? key,
    required this.id,
  }) : super(key: key);

  @override
  _CourseDetailbyIDState createState() => _CourseDetailbyIDState();
}

class _CourseDetailbyIDState extends State<CourseDetailbyID> {
  int likes = 0;
  List<String> comments = [];
  bool _isLiked = false;
  TraildetailModel? trailDetail;

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
  void initState() {
    super.initState();
    fetchTrailDetail();
  }

  void fetchTrailDetail() {
    final apiService = TrailApiService();
    apiService.getRequest('course/individaul/${widget.id}').then((response) {
      setState(() {
        trailDetail = TraildetailModel.fromJson(jsonDecode(response.body));
      });
    }).catchError((error) {
      // 오류 처리
      print('오류 발생: $error');
    });
  }

  @override
  Widget build(BuildContext context) {
    if (trailDetail == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Course Detail'),
        ),
        body: const Center(
          child: CircularProgressIndicator(), // 로딩 중 표시
        ),
      );
    }

    if (trailDetail!.id == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Course Detail'),
        ),
        body: const Center(
          child: Text('데이터가 없습니다.'), // 데이터 없음을 알리는 메시지
        ),
      );
    }

    final double lengthInKm = trailDetail!.distance / 1000;
    final formattedDate = DateFormat("MM/dd").format(trailDetail!.createdDate);

    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        title: Text(trailDetail!.title),
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
                    trailDetail!.title,
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
                '시작위치: ${trailDetail!.startLocationName}',
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
                children: trailDetail!.tags.map((keyword) {
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
