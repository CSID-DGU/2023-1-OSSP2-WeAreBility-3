//산책로 페이지 Trail()
import 'package:flutter/material.dart';
import 'package:naemansan/models/trailmodel.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/services/api_service.dart';

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

  ListView makeList(AsyncSnapshot<List<TrailModel>> snapshot) {
    return ListView.separated(
      scrollDirection: Axis.vertical,
      itemCount: snapshot.data!.length,
      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 20),
      itemBuilder: (context, index) {
        var trail = snapshot.data![index];

        return Trail(
            title: trail.title,
            startpoint: trail.startLocationName,
            distance: trail.distance,
            CourseKeyWord: trail.tags,
            likeCnt: trail.likeCount,
            userCnt: trail.userCount,
            isLiked: trail.isLiked);
      },
      separatorBuilder: (BuildContext context, int index) =>
          const SizedBox(height: 10),
    );
  }

  @override
  //산책로

  Widget build(BuildContext context) {
    final Future<List<TrailModel>> NearestTrail = ApiService.getNearestTrail();
    final Future<List<TrailModel>> MostLikedTrail =
        ApiService.getMostLikedTrail();
    final Future<List<TrailModel>> MostUsedTrail =
        ApiService.getMostUsedTrail();
    final Future<List<TrailModel>> NewTrail = ApiService.getNewTrail();

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
        titleSpacing: 0,
        actions: [
          const SizedBox(width: 16), // 여백 추가
          IconButton(
            icon: const Icon(
              Icons.add_box_outlined, //산책로 추가 시 버튼으로 사용
              color: Colors.black,
            ),
            onPressed: () {},
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
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '좋아요순',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '이용자순',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '최신순',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '키워드',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
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
