//나만의 산책로 페이지 Myrail()
import 'package:flutter/material.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/widgets/widget_trail.dart';
import 'package:naemansan/services/api_service.dart';
import 'package:naemansan/models/trailmodel.dart';

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

  ListView makeList(AsyncSnapshot<List<TrailModel>> snapshot) {
    return ListView.separated(
      scrollDirection: Axis.vertical,
      itemCount: snapshot.data!.length,
      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 20),
      itemBuilder: (context, index) {
        var trail = snapshot.data![index];

        return TrailWidget(
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
  Widget build(BuildContext context) {
    //나만의 산책로
    final Future<List<TrailModel>> CreatedTrail = ApiService.getCreatedTrail();
    final Future<List<TrailModel>> LikedTrail = ApiService.getLikedTrail();
    final Future<List<TrailModel>> UsedTrail = ApiService.getUsedTrail();

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        automaticallyImplyLeading: false, // 앱바의 뒤로가기 버튼을 없애기 위해 false로 설정

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
          // 첫 번째 탭 (등록)
          FutureBuilder(
            future: CreatedTrail,
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return Row(
                  children: [
                    const SizedBox(
                      height: 30,
                    ),
                    Expanded(child: makeList(snapshot))
                  ],
                );
              }
              return const Center(
                child: CircularProgressIndicator(),
              );
            },
          ),
          // 두 번째 탭 (좋아요)
          FutureBuilder(
            future: LikedTrail,
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return Row(
                  children: [
                    const SizedBox(
                      height: 30,
                    ),
                    Expanded(child: makeList(snapshot))
                  ],
                );
              }
              return const Center(
                child: CircularProgressIndicator(),
              );
            },
          ),
          // 세 번째 탭 (이용)
          FutureBuilder(
            future: UsedTrail,
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return Row(
                  children: [
                    const SizedBox(
                      height: 30,
                    ),
                    Expanded(child: makeList(snapshot))
                  ],
                );
              }
              return const Center(
                child: CircularProgressIndicator(),
              );
            },
          ),
          // 네 번째 탭 (댓글)
          const Center(
            child: Text('계정 사용자가 작성한 댓글 모음'),
          ),
          // 다섯 번째 탭 (키워드)
          const Center(
            //팔로우한 키워드 보기
            child: Text('키워드'),
          ),
        ],
      ),
    );
  }
}
