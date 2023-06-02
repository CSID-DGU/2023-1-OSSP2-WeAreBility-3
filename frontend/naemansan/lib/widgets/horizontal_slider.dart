import 'package:flutter/material.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/widgets/main_card.dart';

class HorizontalSlider extends StatefulWidget {
  final double? latitude;
  final double? longitude;
  final String? keyword;

  const HorizontalSlider({
    Key? key,
    this.latitude,
    this.longitude,
    this.keyword,
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

  Future<void> fetchItems() async {
    ApiService apiService = ApiService();
    List<dynamic>? data;

    if (widget.keyword != null) {
      data = await apiService.getTagBasedCourseList(widget.keyword!);
    } else if (widget.latitude != null && widget.longitude != null) {
      data = await apiService.getLocationBasedCourseList(
          widget.latitude!, widget.longitude!);
    }

    // print(data);

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
    } else {
      print(Error());
      // Handle error when data is null
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
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
    );
  }
}
