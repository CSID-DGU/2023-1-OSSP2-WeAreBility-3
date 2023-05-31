import 'package:flutter/material.dart';
import 'dart:math';

class TrailWidget extends StatelessWidget {
  final String title;
  final String startpoint;
  final double distance;
  final List<String>? CourseKeyWord;
  final int likeCnt, userCnt;
  final bool isLiked;
  //id
  //키워드gi?
  //created data

  const TrailWidget({
    Key? key,
    required this.title,
    required this.startpoint,
    required this.distance,
    this.CourseKeyWord,
    required this.likeCnt,
    required this.userCnt,
    required this.isLiked,
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
                    startpoint,
                    style: const TextStyle(
                      fontSize: 12.0,
                    ),
                  ),
                  Text(
                    '$distance km',
                    style: const TextStyle(
                      fontSize: 12.0,
                    ),
                  ),
                  if (CourseKeyWord != null)
                    Wrap(
                      children: CourseKeyWord!
                          .map((word) => Text(
                                '#$word ',
                                style: const TextStyle(
                                  fontSize: 12.0,
                                  color: Colors.black,
                                ),
                              ))
                          .toList(),
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
                        /* widget_trail.dart 상단, trailmodel.dart 수정 필요 
                        Navigator.push(
          context, 
          MaterialPageRoute(
            builder: (context) => CourseDetail(
              id: id,
              title: title,
              location: startpoint,
              //length: length,
              length: distance,
              likes: likeCnt,
              keywords: CourseKeyWord,
              created_date: created_date,
                      */
                      }, // 산책로 세부 페이지로 이동
                    ),
                  ],
                ),
                const SizedBox(height: 15),
                Row(
                  children: [
                    Icon(
                      isLiked ? Icons.favorite : Icons.favorite_border_outlined,
                      color: isLiked ? Colors.red : null,
                    ),
                    Text(
                      '$likeCnt',
                      style: const TextStyle(
                        fontSize: 16.0,
                      ),
                    ),
                  ],
                ),
                Row(
                  children: [
                    const Icon(Icons.person_outline),
                    Text(
                      '$userCnt',
                      style: const TextStyle(
                        fontSize: 16.0,
                      ),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(width: 4.0),
          ],
        ),
      ),
    );
  }
}
