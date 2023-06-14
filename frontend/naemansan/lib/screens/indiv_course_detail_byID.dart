// 개인 산책로 조회 - 내가 등록한 (나만의) 산책로 /course/individual/{id}

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:naemansan/models/indivtraildetailmodel.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/widgets/detail_map.dart';

class IndivCourseDetailbyID extends StatefulWidget {
  final int id;

  const IndivCourseDetailbyID({
    Key? key,
    required this.id,
  }) : super(key: key);

  @override
  _IndivCourseDetailbyIDState createState() => _IndivCourseDetailbyIDState();
}

class _IndivCourseDetailbyIDState extends State<IndivCourseDetailbyID> {
  IndivTraildetailModel? trailDetail;

  String imageUrl = "";
  String userName = "";

  void addComment(String comment) {
    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    print(widget.id);
    fetchTrailDetail();
    fetchUserInfo();
  }

  Future<void> fetchTrailDetail() async {
    ApiService apiService = ApiService();
    Map<String, dynamic>? data;

    data = await apiService.getIndividualmentCourseDetailById(widget.id);
    print(data);

    if (data != null) {
      setState(() {
        trailDetail = IndivTraildetailModel.fromJson(data!);
      });
    }
  }

  Future<void> fetchUserInfo() async {
    ApiService apiService = ApiService();
    Map<String, dynamic>? userData = await apiService.getUserInfo();
    if (userData != null) {
      String filename = userData['image_path'];
      setState(() {
        imageUrl = 'https://ossp.dcs-hyungjoon.com/image?uuid=$filename';
        userName = userData['name'];
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
                    CircleAvatar(
                      radius: 20,
                      backgroundImage: NetworkImage(imageUrl),
                    ),
                    const SizedBox(width: 15),
                    Text(
                      userName,
                      style: const TextStyle(
                        fontSize: 17,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ],
                ),
              ),
              DetailMap(locations: trailDetail!.locations), //지도 표시
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
                    children: const [],
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
              Text(
                '길이: ${lengthInKm.toStringAsFixed(2)} km',
                style: const TextStyle(
                  fontSize: 18,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
