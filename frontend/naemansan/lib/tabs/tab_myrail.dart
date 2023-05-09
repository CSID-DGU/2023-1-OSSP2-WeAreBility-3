//나만의 산책로 페이지 Myrail()
import 'package:flutter/material.dart';
import 'package:naemansan/screens/screen_index.dart';

class Myrail extends StatefulWidget {
  const Myrail({Key? key}) : super(key: key);

  @override
  _MyrailState createState() => _MyrailState();
}

class _MyrailState extends State<Myrail> with SingleTickerProviderStateMixin {
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
            Icons.arrow_back_ios_outlined, //홈 화면으로 이동 아이콘
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
          '나만의 산책로',
          style: TextStyle(
            fontSize: 24,
            fontWeight: FontWeight.w600,
          ),
        ),
        //---------------------------------------------------------
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: Colors.black, //선택된 항목 나타내기
          tabs: const [
            Tab(
              child: Text(
                '등록한',
                style: TextStyle(color: Colors.black),
              ),
            ),
            //------------------------------------------------------
            Tab(
              child: Text(
                '좋아요',
                style: TextStyle(color: Colors.black),
              ),
            ),
            Tab(
              child: Text(
                '이용',
                style: TextStyle(color: Colors.black),
              ),
            ),
            Tab(
              child: Text(
                '댓글',
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
        children: [
          Center(
            //등록한 산책로
            child: ListView.builder(
              itemBuilder: (BuildContext context, int index) {
                ;
                return null;
              }, //api에서 불러온 데이터에 인덱스를 부여해 목록으로 보여줌
            ),
            //->
          ),
          const Center(
            child: Text('계정 사용자가 좋아요한 산책로 리스트'),
          ),
          const Center(
            child: Text('계정 사용자가 이용한 산책로 리스트'),
          ),
          const Center(
            child: Text('계정 사용자가 작성한 댓글 모음'),
          ),
          const Center(
            child: Text('키워드'),
          ),
        ],
      ),
    );
  }
}
