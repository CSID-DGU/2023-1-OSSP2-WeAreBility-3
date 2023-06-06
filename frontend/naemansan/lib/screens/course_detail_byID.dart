import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:naemansan/models/other_user_model.dart';
import 'package:naemansan/models/traildetailmodel.dart';
import 'package:naemansan/services/login_api_service.dart';

class CourseDetailbyID extends StatefulWidget {
  final int id;
  final int likeCnt;

  const CourseDetailbyID({
    Key? key,
    required this.id,
    required this.likeCnt,
  }) : super(key: key);

  @override
  _CourseDetailbyIDState createState() => _CourseDetailbyIDState();
}

class _CourseDetailbyIDState extends State<CourseDetailbyID> {
  List<String> comments = [];
  TraildetailModel? trailDetail;
  OtherUserModel? otherUser;
  String imageUrl = "";

  void addComment(String comment) {
    setState(() {
      comments.add(comment);
    });
  }

  @override
  void initState() {
    super.initState();
    print(widget.id);
    fetchTrailDetail();
  }

  Future<void> fetchTrailDetail() async {
    ApiService apiService = ApiService();
    Map<String, dynamic>? data;

    data = await apiService.getEnrollmentCourseDetailById(widget.id);
    print(data);
    if (data != null) {
      setState(() {
        trailDetail = TraildetailModel.fromJson(data!);
      });
      fetchWriterProfile();
    }
  }

  // 상대프로필 조회
  Future<void> fetchWriterProfile() async {
    ApiService apiService = ApiService();
    Map<String, dynamic>? data;

    data = await apiService.getOtherUserProfile(trailDetail!.userid);
    if (data != null) {
      setState(() {
        otherUser = OtherUserModel.fromJson(data!);
      });
    }
    setState(() {
      imageUrl =
          'https://ossp.dcs-hyungjoon.com/image?uuid=${otherUser!.imagePath}';
    });
  }

  // 좋아요 POST보내기
  Future<void> postLike() async {
    ApiService apiService = ApiService();
    bool data;

    data = await apiService.likeCourse(widget.id);
    if (data) {
      print("좋아요 하기");
      setState(() {});
    }
  }

  // 좋아요 Delete 보내기
  Future<void> deleteLike() async {
    ApiService apiService = ApiService();
    bool data;

    data = await apiService.unlikeCourse(widget.id);
    if (data) {
      print("좋아요 삭제");
      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    print(trailDetail);
    if (trailDetail == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Course Detail'),
        ),
        body: const Center(
          child: CircularProgressIndicator(),
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
                  children: [
                    CircleAvatar(
                      radius: 20,
                      backgroundImage: NetworkImage(imageUrl),
                    ),
                    const SizedBox(width: 15),
                    Text(trailDetail!.username,
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
                      trailDetail!.isLiked
                          ? Icons.favorite
                          : Icons.favorite_border,
                      color: trailDetail!.isLiked ? Colors.red : null,
                    ),
                    onPressed: () => {
                      // 좋아요 POST보내기
                      trailDetail!.isLiked ? deleteLike() : postLike(),
                    },
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
                '좋아요: ${widget.likeCnt}',
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
