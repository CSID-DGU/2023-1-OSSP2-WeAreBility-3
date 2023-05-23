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

    // final response =
    //     await http.get(Uri.parse('https://jsonplaceholder.typicode.com/posts'));
    // if (response.statusCode == 200) {
    //   final data = json.decode(response.body) as List<dynamic>;
    //   final items = data
    //       .map((item) => SlideItem(
    //             userId: item['userId'],
    //             id: item['id'],
    //             title: item['title'],
    //           ))
    //       .toList();
    //   setState(() {
    //     slideItems = items;
    //   });
    // } else {
    //   // Handle API error
    //   print('Failed to fetch items: ${response.statusCode}');
    // }
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

//https://github.com/seochan99/2023-1-OSSP2-WeAreBility-3/commit/0f819d5afc86810b4c12e83945386ceb9bb15e9a#diff-2e1d24658bba85d2dc2557c5c153b3220e7c38f875272a2452f881d1636e50a0