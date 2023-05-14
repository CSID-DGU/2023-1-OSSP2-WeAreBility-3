import 'package:flutter/material.dart';

class Trail extends StatelessWidget {
  //final String userimage;
  final String title;
  final String startpoint; //시작 위치 불러오기  (Segment ID -startpoint)
  final double distance; //거리 (endpoint - startpoint)
  final List<String>? CourseKeyWord; //여러개일수도 있음
  final int likeCnt, userCnt;
  final bool isLiked;

  const Trail({
    super.key,
    //required this.userimage,
    required this.title,
    required this.startpoint,
    required this.distance,
    this.CourseKeyWord,
    required this.likeCnt,
    required this.userCnt,
    required this.isLiked,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Container(
        height: 100.0,
        decoration: BoxDecoration(
          color: Colors.grey[300],
          borderRadius: BorderRadius.circular(8.0),
        ),
        child: Row(
          children: [
            const SizedBox(
              width: 100.0,
              child: Icon(Icons.image),
            ),
            const SizedBox(width: 8.0),
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
                      fontSize: 10.0,
                    ),
                  ),
                  Text(
                    '$distance' 'km',
                    style: const TextStyle(
                      fontSize: 10.0,
                    ),
                  ),
                  if (CourseKeyWord != null)
                    Text(
                      '$CourseKeyWord',
                      style: const TextStyle(
                        fontSize: 10.0,
                      ),
                    )
                ],
              ),
            ),
            const SizedBox(width: 16.0), // 첫 번째 row와 두 번째 row 사이 간격
            Column(
              children: [
                Row(children: [
                  const SizedBox(width: 4.0, height: 4.0),
                  IconButton(
                    icon: const Icon(Icons.arrow_forward_ios_outlined),
                    onPressed: () {}, //산책로 상세 페이지로 이동
                  ),
                ]),
                const SizedBox(width: 10),
                Row(//좋아요 표시
                    children: [
                  const SizedBox(width: 15), //좋아요 하트 이미지 삽입자리
                  Text('$likeCnt', // 좋아요 수 데이터 불러오기
                      style: const TextStyle(
                        fontSize: 16.0,
                      )),
                ]),
                Row(//이용자 표시
                    children: [
                  const SizedBox(width: 15), //이용자 이미지 삽입자리
                  Text('$userCnt', // 이용자 수 데이터 불러오기
                      style: const TextStyle(
                        fontSize: 16.0,
                      )),
                ]),
                const SizedBox(width: 16.0),
              ],
            ),
            const SizedBox(width: 4.0),
          ],
        ),
      ),
    );
  }
}
