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
  //int likes = 0;
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
        //likes--;
        _isLiked = false;
      } else {
        //likes++;
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
    print('전달받은 아이디= ${widget.id}'); // 확인용
    apiService.getRequest('course/enrollment/${widget.id}').then((response) {
      print('Response: ${response.body}'); // response 출력
      setState(() {
        trailDetail =
            TraildetailModel.fromJson(jsonDecode(response.body)); //<-여기가 문제
      });
    }).catchError((error) {
      // 오류 처리
      print('trailDetail을 불러오지 못함 - 오류: $error');
      // 확인용
    });
  }

  @override
  Widget build(BuildContext context) {
    if (trailDetail == null) {
      // 있는 산책로의 id를 전달했는데 왜 null ...
      print('trailDetail == null');
      return Scaffold(
        appBar: AppBar(
          title: const Text('Course Detail'),
        ),
        body: const Center(
          child: Text('산책로 정보를 불러오는데 실패했습니다'),
        ),
      );
    }
    // null 체크 이후에 .id에 접근

/*
    if (trailDetail!.id == null) {
      print(trailDetail!.id);
      print('trailDetail!.id == null');
      return Scaffold(
        appBar: AppBar(
          title: const Text('Course Detail'),
        ),
        body: const Center(
          child: Text('데이터가 없습니다.'), // 데이터 없음을 알리는 메시지
        ),
      );
    }*/

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
                  children: [
                    const CircleAvatar(
                      radius: 20,
                      backgroundImage: NetworkImage(
                        'https://avatars.githubusercontent.com/u/78739194?v=4',
                      ),
                    ),
                    const SizedBox(width: 15),
                    Text('$trailDetail!.id',
                        style: const TextStyle(
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
              const Text(
                '좋아요',
                //'좋아요: $likes',
                style: TextStyle(
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
