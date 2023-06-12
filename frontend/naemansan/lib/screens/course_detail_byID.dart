import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:naemansan/models/other_user_model.dart';
import 'package:naemansan/models/traildetailmodel.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/widgets/detail_map.dart';
import 'package:naemansan/profile_tabs/view_profile.dart';

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
  List<String> comments = [];
  TraildetailModel? trailDetail;
  OtherUserModel? otherUser;
  String imageUrl = "";
  bool isLikeNow = false;
  int likeCnt = 0;

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
        isLikeNow = trailDetail!.isLiked;
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
    print("POST");
    ApiService apiService = ApiService();
    bool data;

    data = await apiService.likeCourse(widget.id);
    if (data) {
      print("좋아요 성공");
      setState(() {
        isLikeNow = true;
        trailDetail!.likeCnt++;
      });
    }
  }

  // 좋아요 Delete 보내기
  Future<void> deleteLike() async {
    print("삭제!");
    ApiService apiService = ApiService();
    bool data;

    data = await apiService.unlikeCourse(widget.id);
    if (data) {
      print("좋아요 삭제");
      setState(() {
        isLikeNow = false;
        trailDetail!.likeCnt--;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    print(trailDetail);
    if (trailDetail == null) {
      return const Scaffold(
        body: Center(
          child: CircularProgressIndicator(
            color: Colors.black,
          ),
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
                    GestureDetector(
                      onTap: () {
                        // 상대방 프로필로 이동
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            //-------------------------------------------------------------------------------------------------------------
                            builder: (context) => ViewProfile(
                              userId: trailDetail!.userid,
                              // 상대방 프로필 조회
                            ),
                          ),
                        );
                      },
                      child: CircleAvatar(
                        radius: 20,
                        backgroundImage: NetworkImage(imageUrl),
                      ),
                    ),
                    const SizedBox(width: 15),
                    Text(trailDetail!.username,
                        style: const TextStyle(
                            fontSize: 17, fontWeight: FontWeight.w500)),
                  ],
                ),
              ),

              DetailMap(locations: trailDetail!.locations),

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
                  Column(
                    children: [
                      IconButton(
                        icon: Icon(
                          isLikeNow ? Icons.favorite : Icons.favorite_border,
                          color: isLikeNow ? Colors.red : null,
                        ),
                        onPressed: () => {
                          // 좋아요 POST보내기
                          isLikeNow ? deleteLike() : postLike(),
                        },
                      ),
                      Text(
                        '${trailDetail!.likeCnt}',
                        style: const TextStyle(
                          fontSize: 16,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
              Text(
                '생성 날짜: ${DateFormat('yy.MM.dd').format(trailDetail!.createdDate)}',
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
                  fontSize: 18,
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
