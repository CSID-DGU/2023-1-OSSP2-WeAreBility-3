import 'package:flutter/material.dart';
import 'package:naemansan/models/mytap_trail_model.dart';
import 'package:naemansan/screens/map/naver_map_screen.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/widgets/widget_mytrail.dart';
import 'package:naemansan/widgets/widget_trail.dart';
import 'package:naemansan/models/trailmodel.dart';
//import 'package:naemansan/models/traildetailmodel.dart';
import 'package:naemansan/services/courses_api.dart';
import 'package:naemansan/models/trailcommentmodel.dart';
import 'package:naemansan/widgets/widget_trailcomment.dart';
import 'package:naemansan/screens/create_course_screen.dart';

class Myrail extends StatefulWidget {
  final int initialTabIndex;
  const Myrail({Key? key, this.initialTabIndex = 0}) : super(key: key);

  @override
  _MyrailState createState() => _MyrailState();
}

class _MyrailState extends State<Myrail> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  late TrailApiService TrailapiService;

  int openIndex = 1; // 나만의, 공개 산책로 등록시 사용하는 인덱스
  int selectedIndex = 0; // 키워드별 보기에서 사용하는 인덱스 !!

  @override
  void initState() {
    super.initState();
    TrailapiService = TrailApiService();
    _tabController = TabController(
      length: 5,
      initialIndex: widget.initialTabIndex,
      vsync: this,
    );
    openIndex = 0;
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  ListView makeList(AsyncSnapshot<List<dynamic>?> snapshot) {
    return ListView.separated(
      scrollDirection: Axis.vertical,
      itemCount: snapshot.data!.length,
      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 20),
      itemBuilder: (context, index) {
        var data = snapshot.data![index];
// --------------------------------------- 탭별 위젯 디자인 선택
        if (data is TrailModel) {
          var trail = data;

          return TrailWidget(
            title: trail.title,
            startpoint: trail.startLocationName,
            distance: trail.distance / 1000,
            CourseKeyWord: trail.tags,
            likeCnt: trail.likeCount,
            userCnt: trail.userCount,
            isLiked: trail.isLiked,
            id: trail.id,
            created_date: trail.createdDate.toString(),
          );
        } else if (data is TrailCommentModel) {
          var trail = data;

          return CommentTrailWidget(
            id: trail.id,
            courseId: trail.courseId,
            title: trail.title,
            tags: trail.tags,
            content: trail.content,
          );
        } else if (data is MytabTrailModel) {
          var trail = data;

          return MyTrailWidget(
            title: trail.title,
            distance: trail.distance / 1000,
            id: trail.id,
            created_date: trail.createdDate.toString(),
          );
        }

        return const SizedBox();
      },
      separatorBuilder: (BuildContext context, int index) =>
          const SizedBox(height: 20),
    );
  }

  @override
  Widget build(BuildContext context) {
    const int page = 0;
    const int num = 100000000;
    List<String> keywords = [
      '힐링',
      '스타벅스',
      '은평구',
      '출근길'
    ]; //임시 키워드 설정()->추후 내가 설정한 키워드 불러오기로 바꾸어야함!!

    final Future<List<TrailModel>?> EnrolledTrail =
        TrailapiService.getEnrolledCourses(page, num);
    final Future<List<MytabTrailModel>?> IndivTrail =
        TrailapiService.getIndividualBasicCourses(page, num);
    final Future<List<TrailModel>?> LikedTrail =
        TrailapiService.getLikedCourses(page, num);
    final Future<List<TrailModel>?> UsedTrail =
        TrailapiService.getUsedCourses(page, num);
    final Future<List<TrailCommentModel>?> CommentedTrail =
        TrailapiService.getCommentedCourses(page, num);
    final Future<List<TrailModel>?> KeyWordTrail =
        TrailapiService.getKeywordCourse(page, num, keywords[selectedIndex]);

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
                builder: (BuildContext context) => const IndexScreen(index: 0),
              ),
            );
          },
        ),
        title: const Text(
          '나만의 산책로',
          style: TextStyle(
            fontSize: 21,
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
                  //산책로 등록
                  builder: (context) => const NaverMapScreen(),
                  // builder: (context) => const CreateCourseScreen(),
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
          //첫번째 탭
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    TextButton(
                      onPressed: () {
                        setState(() {
                          openIndex = 0;
                        });
                      },
                      style: ButtonStyle(
                        backgroundColor:
                            MaterialStateProperty.resolveWith<Color>((states) {
                          if (states.contains(MaterialState.pressed) ||
                              openIndex == 0) {
                            return const Color.fromARGB(255, 26, 167, 85);
                          }
                          return Colors.white;
                        }),
                        shape:
                            MaterialStateProperty.all<RoundedRectangleBorder>(
                          RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20),
                          ),
                        ),
                        elevation: MaterialStateProperty.all(1.0),
                      ),
                      child: Text(
                        '나만의', //EnrolledTrail
                        style: TextStyle(
                          color: openIndex == 0 ? Colors.white : Colors.black,
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    TextButton(
                      onPressed: () {
                        setState(() {
                          openIndex = 1;
                        });
                      },
                      style: ButtonStyle(
                        backgroundColor:
                            MaterialStateProperty.resolveWith<Color>((states) {
                          if (states.contains(MaterialState.pressed) ||
                              openIndex == 1) {
                            return const Color.fromARGB(255, 26, 167, 85);
                          }
                          return Colors.white;
                        }),
                        shape:
                            MaterialStateProperty.all<RoundedRectangleBorder>(
                          RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20),
                          ),
                        ),
                        elevation: MaterialStateProperty.all(1.0),
                      ),
                      child: Text(
                        '모두의', //EnrolledTrail
                        style: TextStyle(
                          color: openIndex == 1 ? Colors.white : Colors.black,
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                Expanded(
                  //---------------------------산책로 정보 조회 --------------------
                  child: FutureBuilder(
                    future: openIndex == 1 ? EnrolledTrail : IndivTrail,
                    builder: (context, snapshot) {
                      if (snapshot.hasData) {
                        if (snapshot.data!.isNotEmpty) {
                          return makeList(snapshot);
                        } else {
                          // 산책로 없을 때 추가 ---
                          return Center(
                            child: IconButton(
                              icon: const Icon(Icons.add),
                              onPressed: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) =>
                                        const CreateCourseScreen(),
                                  ),
                                );
                              },
                            ),
                          );
                        }
                      } else if (!snapshot.hasData) {
                        return Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            if (openIndex == 0)
                              IconButton(
                                icon: const Icon(Icons.add),
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) =>
                                          const CreateCourseScreen(),
                                    ),
                                  );
                                },
                              ),
                            if (openIndex == 0)
                              const Text('산책로 등록하러 가기')
                            else if (openIndex == 1)
                              const Text('공개 등록한 산책로가 없습니다.')
                          ],
                        );
                      }
                      return const Center(
                        child: CircularProgressIndicator(
                          color: Colors.black,
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),

          // 두 번째 탭--------------------------------------------------------------------------------
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
                  child: CircularProgressIndicator(
                    color: Colors.black,
                  ),
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
                  child: CircularProgressIndicator(
                    color: Colors.black,
                  ),
                );
              },
            ),
          ),
          // 네 번째 탭
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: FutureBuilder(
              future: CommentedTrail,
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
                  child: CircularProgressIndicator(
                    color: Colors.black,
                  ),
                );
              },
            ),
          ),

          // 다섯 번째 탭
          Padding(
            padding: const EdgeInsets.only(left: 16.0, top: 10),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Row(
                    children: List<Widget>.generate(
                      keywords.length,
                      (index) {
                        final keyword = keywords[index];
                        bool isSelected = index == selectedIndex;

                        return Row(
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(right: 8.0),
                              child: TextButton(
                                onPressed: () {
                                  setState(() {
                                    selectedIndex = index;
                                  });
                                },
                                style: ButtonStyle(
                                  backgroundColor:
                                      MaterialStateProperty.resolveWith<Color>(
                                    (Set<MaterialState> states) {
                                      if (isSelected) {
                                        return const Color.fromARGB(
                                            255, 26, 167, 85);
                                      }
                                      return Colors.white;
                                    },
                                  ),
                                  shape: MaterialStateProperty.all<
                                      RoundedRectangleBorder>(
                                    RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(20),
                                    ),
                                  ),
                                  elevation: MaterialStateProperty.all(1.0),
                                ),
                                child: Text(
                                  keyword,
                                  style: TextStyle(
                                    color: isSelected
                                        ? Colors.white
                                        : Colors.black,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                          ],
                        );
                      },
                    ),
                  ),
                ),
                const SizedBox(height: 10),
                Expanded(
                  child: FutureBuilder(
                    future: KeyWordTrail,
                    builder: (context, snapshot) {
                      if (snapshot.hasData) {
                        if (snapshot.data!.isNotEmpty) {
                          return makeList(snapshot);
                        } else {
                          return const Center(
                            child: Text('해당 산책로가 없습니다'),
                          );
                        }
                      }
                      return const Center(
                        child: CircularProgressIndicator(
                          color: Colors.black,
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
