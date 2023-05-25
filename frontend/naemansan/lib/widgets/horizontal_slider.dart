import 'package:flutter/material.dart';
import 'package:naemansan/widgets/main_card.dart';

class HorizontalSlider extends StatefulWidget {
  const HorizontalSlider({super.key});

  @override
  _HorizontalSliderState createState() => _HorizontalSliderState();
}

class _HorizontalSliderState extends State<HorizontalSlider> {
  List<SlideItem> slideItems = [];

  @override
  void initState() {
    super.initState();
    fetchItems();
  }

  Future<void> fetchItems() async {
    List<Map<String, dynamic>> dummbyData = [
      {
        "id": 1,
        "title": '힐링 숲속',
        "location": '경기도 가평군 설악면',
        "length": '1.5km',
        "likes": 100,
        "keywords": ['힐링', '도심'],
      },
      {
        "id": 2,
        "title": '히찬 코스',
        "location": '경기도 가평군 설악면',
        "length": '1.5km',
        "likes": 50,
        "keywords": ['힐링', '도심'],
      },
      {
        "id": 3,
        "title": '디디피',
        "location": '경기도 가평군 설악면',
        "length": '1.5km',
        "likes": 75,
        "keywords": ['힐링', '도심'],
      },
      {
        "id": 4,
        "title": '충무 코스',
        "location": '경기도 가평군 설악면',
        "length": '1.5km',
        "likes": 100,
        "keywords": ['힐링', '도심'],
      },
      {
        "id": 5,
        "title": '힐링 숲속',
        "location": '경기도 가평군 설악면',
        "length": '1.5km',
        "likes": 50,
        "keywords": ['힐링', '도심'],
      },
      {
        "id": 6,
        "title": '힐링 숲속',
        "location": '경기도 가평군 설악면',
        "length": '1.5km',
        "likes": 75,
        "keywords": ['힐링', '도심'],
      },
    ];

    final items = dummbyData
        .map((item) => SlideItem(
              id: item['id'],
              title: item['title'],
              location: item['location'],
              length: item['length'],
              likes: item['likes'],
              keywords: item['keywords'],
            ))
        .toList();
    setState(() {
      slideItems = items;
    });

    // 찐 데이터

//     List<dynamic>? tagBasedCourses =
//         await ApiService.getTagBasedCourseList('태그명');
//     if (tagBasedCourses != null) {
//       // 성공적으로 조회된 경우 처리 로직
//       // tagBasedCourses 리스트를 사용하여 UI 업데이트 등을 수행
//     } else {
//       // 조회 실패 또는 에러 발생 시 처리 로직
//     }

// // 위치 기반 산책로 조회
//     List<dynamic>? locationBasedCourses =
//         await ApiService.getLocationBasedCourseList(37.12345, 127.12345);
//     if (locationBasedCourses != null) {
//       // 성공적으로 조회된 경우 처리 로직
//       // locationBasedCourses 리스트를 사용하여 UI 업데이트 등을 수행
//     } else {
//       // 조회 실패 또는 에러 발생 시 처리 로직
//     }
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      scrollDirection: Axis.horizontal,
      itemCount: slideItems.length,
      itemExtent: 190, // 아이템의 높이를 200으로 고정

      itemBuilder: (context, index) {
        return CardWidget(
          id: slideItems[index].id,
          title: slideItems[index].title,
          location: slideItems[index].location,
          length: slideItems[index].length,
          likes: slideItems[index].likes,
          keywords: slideItems[index].keywords,
        );
      },
    );
  }
}
