//산책로 페이지 Trail()
import 'package:flutter/material.dart';
import 'package:naemansan/screens/screen_index.dart';

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
            fontSize: 24,
            fontWeight: FontWeight.w600,
          ),
        ),
        actions: [
          IconButton(
            icon: const Icon(
              Icons.add_box_outlined, //산책로 추가 시 버튼으로 사용
              color: Colors.black,
            ),
            onPressed: () {},
          ),
        ],
        //오른쪽 여백 넣기
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: Colors.black, //선택된 항목 나타내기
          tabs: const [
            Tab(
              child: Text(
                '거리순',
                style: TextStyle(color: Colors.black),
              ),
            ),
            Tab(
              child: Text(
                '좋아요순',
                style: TextStyle(color: Colors.black),
              ),
            ),
            Tab(
              child: Text(
                '이용자순',
                style: TextStyle(color: Colors.black),
              ),
            ),
            Tab(
              child: Text(
                '최신순',
                style: TextStyle(color: Colors.black),
              ),
            ),
            Tab(
              child: Text(
                '키워드',
                style: TextStyle(color: Colors.black),
              ),
            ),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: const [
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
