import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
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
    final response =
        await http.get(Uri.parse('https://jsonplaceholder.typicode.com/posts'));
    if (response.statusCode == 200) {
      final data = json.decode(response.body) as List<dynamic>;
      final items = data
          .map((item) => SlideItem(
                userId: item['userId'],
                id: item['id'],
                title: item['title'],
              ))
          .toList();
      setState(() {
        slideItems = items;
      });
    } else {
      // Handle API error
      print('Failed to fetch items: ${response.statusCode}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      scrollDirection: Axis.horizontal,
      itemCount: slideItems.length,
      itemExtent: 200, // 아이템의 높이를 200으로 고정

      itemBuilder: (context, index) {
        return CardWidget(
          userId: slideItems[index].userId,
          id: slideItems[index].id,
          title: slideItems[index].title,
        );
      },
    );
  }
}
