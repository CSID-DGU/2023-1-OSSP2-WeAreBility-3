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
          isLiked: trail.isLiked,
          id: trail.id,
          created_date: trail.createdDate.toString(),
        );
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
    final Future<List<TrailModel>?> CommentedTrail =
        TrailapiService.getCommentedCourses(page, num);
    final Future<List<TrailModel>?> KeyWordTrail =
        TrailapiService.getKeywordCourse(page, num, '중구'); //내가 선택한 태그 불러오기 햐

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
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '좋아요',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '이용한',
                style: TextStyle(color: Colors.black, fontSize: 12.5),
              ),
            ),
            Tab(
              child: Text(
                '댓글단',
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
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: EnrolledTrail,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  if (snapshot.data!.isNotEmpty) {
                    return Row(
                      children: [Expanded(child: makeList(snapshot))],
                    );
                  } else {
                    return IconButton(
                      icon: const Icon(Icons.add),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const CreateCourseScreen(),
                          ),
                        );
                      },
                    );
                  }
                }
                return const Center(
                  child: CircularProgressIndicator(),
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
                  if (snapshot.data!.isNotEmpty) {
                    return Row(
                      children: [Expanded(child: makeList(snapshot))],
                    );
                  } else {
                    return const Center(
                      child: Text('좋아요한 산책로가 없습니다'),
                    );
                  }
                }
                return const Center(
                  child: CircularProgressIndicator(),
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
                  if (snapshot.data!.isNotEmpty) {
                    return Row(
                      children: [Expanded(child: makeList(snapshot))],
                    );
                  } else {
                    return const Center(
                      child: Text('이용한 산책로가 없습니다'),
                    );
                  }
                }
                return const Center(
                  child: CircularProgressIndicator(),
                );
              },
            ),
          ),
          // 네 번째 탭
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: CommentedTrail, //나중에 고챠
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  if (snapshot.data!.isNotEmpty) {
                    return Row(
                      children: [Expanded(child: makeList(snapshot))],
                    );
                  } else {
                    return const Center(
                      child: Text('댓글을 작성한 산책로가 없습니다'),
                    );
                  }
                }
                return const Center(
                  child: CircularProgressIndicator(),
                );
              },
            ),
          ),
          // 다섯 번째 탭
          //  Column(
          //  children: [
          // Row(children: Text()), //팔로우한 키워드 보기 하지말까여
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: KeyWordTrail,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  if (snapshot.data!.isNotEmpty) {
                    return Row(
                      children: [Expanded(child: makeList(snapshot))],
                    );
                  } else {
                    return const Center(
                      child: Text('해당 산책로가 없습니다'),
                    );
                  }
                }
                return const Center(
                  child: CircularProgressIndicator(),
                );
              },
            ),
          ),
          //  ],
          //),
        ],
      ),
    );
  }
}
