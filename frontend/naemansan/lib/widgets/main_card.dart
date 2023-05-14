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

    List<IconData> iconList = [
      Icons.nature,
      Icons.nature_outlined,
      Icons.nature_people_outlined,
      Icons.nature_people_sharp,
    ];

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 10),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.7),
            blurRadius: 5.0,
            spreadRadius: 0.0,
          ),
        ],
      ),
      child: Card(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(displayedTitle,
                style: const TextStyle(
                  fontSize: 25,
                  fontWeight: FontWeight.w700,
                )),
            Text(
              location,
              style: const TextStyle(
                fontSize: 14,
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(iconList[id % 4]),
                Text(length),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
