//산책로 페이지 Trail()
import 'package:flutter/material.dart';
import 'package:naemansan/models/trailmodel.dart';
import 'package:naemansan/screens/public_course_detail_screen.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/service/api_service.dart';

class Trail extends StatefulWidget {
  const Trail({Key? key}) : super(key: key);

  @override
  _TrailState createState() => _TrailState();
}

class _TrailState extends State<Trail> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 5, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final Future<List<TrailModel>> TestTrail = ApiService.getTrail();
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back_ios_outlined, //산책로 추가 시 버튼으로 사용
            color: Colors.black,
          ),
          onPressed: () {
            //arrow 아이콘 클릭 시 홈 화면으로 이동 (홈 화면에 화살표 생김)
            false;
            Navigator.of(context).push(
              MaterialPageRoute(
                builder: (BuildContext context) => const IndexScreen(),
              ),
            );
          },
        ),
        title: const Text(
          '산책로',
          style: TextStyle(
            fontSize: 21,
            fontWeight: FontWeight.w600,
          ),
        ),
        titleSpacing: 0,
        actions: [
          const SizedBox(width: 16), // 여백 추가
          IconButton(
            icon: const Icon(
              Icons.add_box_outlined, //산책로 추가 시 버튼으로 사용
              color: Colors.black,
            ),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) =>
                        const PublicCourseDetailScreen(courseName: "공소 코스")),
              );
            },
          ),
          const SizedBox(width: 10)
        ],
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: Colors.black, //선택된 항목 나타내기
          tabs: const [
            Tab(
              child: Text(
                '거리순',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '좋아요순',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '이용자순',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '최신순',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '키워드',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: const [
          // Padding(
          //   padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8.0),
          //   child: Container(
          //     decoration: BoxDecoration(
          //       color: Colors.white,
          //       border: Border.all(
          //         color: Colors.grey.withOpacity(0.5),
          //         width: 1,
          //       ),
          //       borderRadius: BorderRadius.circular(16),
          //     ),
          //     child: Row(
          //       children: [
          //         const SizedBox(
          //           width: 100.0,
          //           child: Icon(Icons.nature_outlined),
          //         ),
          //         const SizedBox(width: 4.0),
          //         Expanded(
          //           child: Column(
          //             mainAxisAlignment: MainAxisAlignment.center,
          //             crossAxisAlignment: CrossAxisAlignment.start,
          //             children: const [
          //               Text(
          //                 "공소 코스",
          //                 style: TextStyle(
          //                   fontSize: 18.0,
          //                   fontWeight: FontWeight.bold,
          //                 ),
          //               ),
          //               SizedBox(height: 4.0),
          //               Text(
          //                 "서울 특별시 중구",
          //                 style: TextStyle(
          //                   fontSize: 12.0,
          //                 ),
          //               ),
          //               Text(
          //                 '0.1km',
          //                 style: TextStyle(
          //                   fontSize: 12.0,
          //                 ),
          //               ),
          //             ],
          //           ),
          //         ),
          //         Column(
          //           children: [
          //             Row(
          //               children: [
          //                 const SizedBox(width: 8.0, height: 4.0),
          //                 IconButton(
          //                   icon: const Icon(Icons.arrow_forward_ios_outlined),
          //                   onPressed: () {}, //산책로 세부 페이지로 이동
          //                 ),
          //               ],
          //             ),
          //             const SizedBox(height: 15),
          //             Row(
          //               children: const [
          //                 Icon(
          //                   true
          //                       ? Icons.favorite
          //                       : Icons.favorite_border_outlined,
          //                   color: true ? Colors.red : null,
          //                   size: 20,
          //                 ),
          //                 Text(
          //                   '1',
          //                   style: TextStyle(
          //                     fontSize: 14.0,
          //                   ),
          //                 ),
          //               ],
          //             ),
          //             Row(
          //               children: const [
          //                 Icon(
          //                   Icons.person_outline,
          //                   size: 20,
          //                 ),
          //                 Text(
          //                   '0',
          //                   style: TextStyle(
          //                     fontSize: 14.0,
          //                   ),
          //                 ),
          //               ],
          //             ),
          //           ],
          //         ),
          //         const SizedBox(width: 4.0),
          //       ],
          //     ),
          //   ),
          // ),
          Center(
            child: Text('거리순 정렬 리스트 추가'),
          ),
          Center(
            child: Text('좋아요순 정렬 리스트 추가'),
          ),
          Center(
            child: Text('이용자순 정렬 리스트 추가'),
          ),
          Center(
            child: Text('최신순 정렬 리스트 추가'),
          ),
          Center(
            child: Text('키워드별 보기 기능 추가'),
          ),
        ],
      ),
    );
  }
}
