//widget_trail.dart

import 'package:flutter/material.dart';
import 'dart:math';
import 'package:naemansan/screens/indiv_course_detail_byID.dart';
//import 'package:naemansan/screens/course_detail_byID.dart';

//산책로 목록 조회에 사용
//산책로 탭 전체 - 추천순, 거리순, 좋아요순, 이용자순, 최신순
//나만의 산책로 탭 - 등록한, 좋아요한, 이용한, 키워드
class MyTrailWidget extends StatelessWidget {
  final String title;

  final double distance;
  final int id;
  final String created_date;

  const MyTrailWidget({
    Key? key,
    required this.title,
    required this.distance,
    required this.id,
    required this.created_date,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final random = Random();
    //'package:naemansan/widgets/main_card.dart'과 동일
    final iconList = [
      Icons.nature,
      Icons.nature_outlined,
      Icons.nature_people_outlined,
      Icons.nature_people_sharp, //
      Icons.directions_walk_rounded,
      Icons.run_circle_outlined,
      Icons.art_track_outlined,
    ];

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 0.0, vertical: 0.0),
      child: Container(
        decoration: BoxDecoration(
          border: Border.all(
            color: Colors.grey.withOpacity(0.5),
            width: 1,
          ),
          borderRadius: BorderRadius.circular(15),
          color: Colors.white,
        ),
        height: 140.0,
        child: Row(
          children: [
            SizedBox(
              width: 100.0,
              child: Icon(iconList[random.nextInt(4)]),
            ),
            const SizedBox(width: 4.0),
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    title,
                    style: const TextStyle(
                      fontSize: 18.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4.0),
                  Text(
                    created_date.substring(0, 10),
                    style: const TextStyle(
                      fontSize: 12.0,
                    ),
                  ),
                  Text(
                    '${distance.toStringAsFixed(2)} km',
                    style: const TextStyle(
                      fontSize: 12.0,
                    ),
                  ),
                ],
              ),
            ),
            Column(
              children: [
                Row(
                  children: [
                    const SizedBox(width: 8.0, height: 4.0),
                    IconButton(
                      icon: const Icon(Icons.arrow_forward_ios_outlined),
                      onPressed: () {
                        print('id: $id, $title로 이동중');
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            //-------------------------------------------------------------------------------------------------------------
                            builder: (context) => IndivCourseDetailbyID(
                              id: id, // 개인 산책로의 디테일 페이지
                              // 산책로 세부 페이지로 이동 -> ID 값 전달
                            ),
                          ),
                        );
                      },
                    ),
                  ],
                ),
                const SizedBox(height: 15),
              ],
            ),
            const SizedBox(width: 4.0),
          ],
        ),
      ),
    );
  }
}
