import 'package:flutter/material.dart';
import 'package:naemansan/screens/map/naver_map_screen.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/widgets/main_card.dart';

class HorizontalSlider extends StatefulWidget {
  final double? latitude;
  final double? longitude;
  final String? keyword;
  final String? title;

  const HorizontalSlider({
    Key? key,
    this.latitude,
    this.longitude,
    this.keyword,
    this.title,
  }) : super(key: key);

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

  // 아이템 Fetch하기
  Future<void> fetchItems() async {
    ApiService apiService = ApiService();
    List<dynamic>? data;

    // 키워드 기반
    if (widget.keyword != null) {
      data = await apiService.getTagBasedCourseList(widget.keyword!);
    }
    // 위치기반
    else if (widget.latitude != null && widget.longitude != null) {
      data = await apiService.getLocationBasedCourseList(
          widget.latitude!, widget.longitude!);
    }

    // 데이터가 있을때
    if (data != null) {
      final items = data
          .map((item) => SlideItem(
                id: item['id'],
                title: item['title'],
                location: item['start_location_name'],
                length: item['distance'],
                likes: item['like_cnt'],
                keywords:
                    List<String>.from(item['tags'].map((tag) => tag['name'])),
                created_date: item['created_date'],
              ))
          .toList();

      if (mounted) {
        setState(() {
          slideItems = items;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return slideItems.isNotEmpty
        ? ListView.builder(
            scrollDirection: Axis.horizontal,
            itemCount: slideItems.length,
            itemExtent: 190, // Set the item height to 200
            itemBuilder: (context, index) {
              return CardWidget(
                id: slideItems[index].id,
                title: slideItems[index].title,
                location: slideItems[index].location,
                length: slideItems[index].length,
                likes: slideItems[index].likes,
                keywords: slideItems[index].keywords,
                created_date: slideItems[index].created_date,
              );
            },
          )
        : Center(
            child: widget.title == "🍽️ 상권"
                ? const Text(
                    '등록된 산책로가 없습니다!',
                    style: TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w600,
                      color: Colors.black87,
                    ),
                  )
                : Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Text(
                        widget.title == "🌿 위치별"
                            ? '현재 위치에 등록된 산책로가 없습니다!'
                            : "'해당하는 키워드의 산책로가 없습니다!'",
                        style: const TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.w600,
                          color: Colors.black87,
                        ),
                      ),
                      ElevatedButton.icon(
                        onPressed: () => {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => const NaverMapScreen()),
                          )
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.white,
                          foregroundColor: Colors.white,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8),
                            side: const BorderSide(color: Colors.black87),
                          ),
                        ),
                        icon: const Icon(
                          Icons.add,
                          color: Colors.black87,
                        ),
                        label: const Text(
                          '산책로 등록하러 가기',
                          style: TextStyle(
                            fontSize: 15,
                            fontWeight: FontWeight.w500,
                            color: Colors.black87,
                          ),
                        ),
                      ),
                    ],
                  ));
  }
}
