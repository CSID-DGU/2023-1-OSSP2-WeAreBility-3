import 'package:flutter/material.dart';
import 'package:naemansan/screens/create_course_screen.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/widgets/widget_trail.dart';
import 'package:naemansan/models/trailmodel.dart';
import 'package:naemansan/services/courses_api.dart';

class Myrail extends StatefulWidget {
  const Myrail({Key? key}) : super(key: key);

  @override
  _MyrailState createState() => _MyrailState();
}

class _MyrailState extends State<Myrail> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  late TrailApiService TrailapiService;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 5, vsync: this);
    TrailapiService = TrailApiService();
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  ListView makeList(AsyncSnapshot<List<TrailModel>?> snapshot) {
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
          const SizedBox(height: 20),
    );
  }

  @override
  Widget build(BuildContext context) {
    const int page = 0;
    const int num = 100000000;

    final Future<List<TrailModel>?> EnrolledTrail =
        TrailapiService.getEnrolledCourses(page, num);
    final Future<List<TrailModel>?> LikedTrail =
        TrailapiService.getLikedCourses(page, num);
    final Future<List<TrailModel>?> UsedTrail =
        TrailapiService.getUsedCourses(page, num);

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back_ios_outlined,
            color: Colors.black,
          ),
          onPressed: () {
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
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => const CreateCourseScreen(),
                ),
              );
            },
          ),
        ],
        titleSpacing: 0,
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: Colors.black,
          tabs: const [
            Tab(
              child: Text(
                '등록한',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '좋아요',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '이용',
                style: TextStyle(color: Colors.black, fontSize: 13.5),
              ),
            ),
            Tab(
              child: Text(
                '댓글',
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
        children: [
          /* 등록된 산책로가 없을 경우 
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => const CreateCourseScreen(),
                ),
              );
            },
          ),
          */
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: EnrolledTrail,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  return Row(
                    children: [Expanded(child: makeList(snapshot))],
                  );
                }
                return const Center(
                  child: CircularProgressIndicator(), //gma
                );
              },
            ),
          ),

          // 두 번째 탭
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: LikedTrail,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  return Row(
                    children: [Expanded(child: makeList(snapshot))],
                  );
                }
                return const Center(
                  child: CircularProgressIndicator(), //gma
                );
              },
            ),
          ),
          // 세 번째 탭
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: UsedTrail,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  return Row(
                    children: [Expanded(child: makeList(snapshot))],
                  );
                }
                return const Center(
                  child: CircularProgressIndicator(), //gma
                );
              },
            ),
          ),
          // 네 번째 탭
          const Center(
            child: Text('계정 사용자가 작성한 댓글 모음'),
          ),
          // 다섯 번째 탭
          const Center(
            child: Text('키워드'),
          ),
        ],
      ),
    );
  }
}
