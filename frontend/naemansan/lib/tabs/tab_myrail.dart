//나만의 산책로 페이지 Myrail()
import 'package:flutter/material.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/service/api_service.dart';
import 'package:naemansan/models/trailmodel.dart';

class Myrail extends StatefulWidget {
  Myrail({Key? key}) : super(key: key);

  final Future<List<TrailModel>> trail = CreatedTrailApi.getCreatedTrail();

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
          //-------------------------------등록한 산책로 탭------------------------------------------
          FutureBuilder<List<TrailModel>>(
            future: widget.trail,
            builder: (BuildContext context,
                AsyncSnapshot<List<TrailModel>> snapshot) {
              if (snapshot.hasData) {
                List<TrailModel> trails = snapshot.data!;
                // trails 리스트를 사용하여 등록한 산책로를 표시하는 위젯을 반환
                return ListView.builder(
                  itemCount: trails.length,
                  itemBuilder: (BuildContext context, int index) {
                    return ListTile(
                      title: Text(trails[index].title),
                      // **위젯 형태에 맞게 수정
                    );
                  },
                );
              } else if (snapshot.hasError) {
                // 에러가 발생한 경우 에러 메시지를 표시하는 위젯을 반환
                return Text('${snapshot.error}');
              } else {
                // 데이터가 로드되기 전에는 로딩 중임을 알리는 위젯을 반환
                return const Center(child: CircularProgressIndicator());
              }
            },
          ),

          //------------------------------좋아요한 산책로 탭------------------------------------------
          const Center(
            child: Text('계정 사용자가 좋아요한 산책로 리스트'),
          ),
          //------------------------------이용한 산책로 탭---------------------------------------------
          const Center(
            child: Text('계정 사용자가 이용한 산책로 리스트'),
          ),
          //-------------------------------댓글 단 산책로 탭-------------------------------------------
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
