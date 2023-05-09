//나만의 산책로 - 등록한 기준

import 'package:flutter/material.dart';

abstract class Trail extends StatelessWidget {
  //임시로 abstract 사용, 필요 파일에 입력 후 abstract제거하기

  //userimage
  final String title;
  //시작 위치 불러오기  (Segment ID -startpoint)
  //거리 (endpoint - startpoint)
  final String? CourseKeyWord; //여러개일수도 있음
  //좋아요 수
  //이용자 수

  const Trail({
    super.key,
    required this.title,
    //required this.startpoint,
    //required this.distance,
    this.CourseKeyWord,
    //required this.heartcounts
    //required this.usedcounts
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
                  const Text(
                    '산책로 시작 위치\n',
                    style: TextStyle(
                      fontSize: 10.0,
                    ),
                  ),
                  const Text(
                    '거리\n',
                    style: TextStyle(
                      fontSize: 10.0,
                    ),
                  ),
                  if (CourseKeyWord != null)
                    Text(
                      CourseKeyWord!,
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
                    children: const [
                  SizedBox(width: 15), //좋아요 하트 이미지 삽입자리
                  Text('좋아요 수', // 좋아요 수 데이터 불러오기
                      style: TextStyle(
                        fontSize: 16.0,
                      )),
                ]),
                Row(//이용자 표시
                    children: const [
                  SizedBox(width: 15), //이용자 이미지 삽입자리
                  Text('이용자수', // 이용자 수 데이터 불러오기
                      style: TextStyle(
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
