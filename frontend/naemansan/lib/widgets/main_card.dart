import 'package:flutter/material.dart';

class SlideItem {
  final int id;
  final String title;
  final String location;
  final String length;
  final int likes;
  final List<String> keywords;

  SlideItem({
    required this.id,
    required this.title,
    required this.location,
    required this.length,
    required this.likes,
    required this.keywords,
  });
}

class CardWidget extends StatelessWidget {
  final int id;
  final String title;
  final String location;
  final String length;
  final int likes;
  final List<String> keywords;

  const CardWidget({
    super.key,
    required this.id,
    required this.title,
    required this.location,
    required this.length,
    required this.likes,
    required this.keywords,
  });

  @override
  Widget build(BuildContext context) {
    String displayedTitle =
        title.length > 6 ? "${title.substring(0, 6)}..." : title;

    return Card(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(displayedTitle,
              style: const TextStyle(
                fontSize: 25,
                fontWeight: FontWeight.w700,
              )),
          ListTile(
            title: Text(
              location,
              style: const TextStyle(
                fontSize: 14,
              ),
            ),
          ),
          ListTile(
            title: Text(length),
          ),
        ],
      ),
    );
  }
}
