import 'package:flutter/material.dart';
import 'package:naemansan/screens/course_detail.dart';

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

    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => CourseDetail(
              id: id,
              title: title,
            ),
          ),
        );
      },
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 10),
        decoration: BoxDecoration(
          border: Border.all(
            color: Colors.grey.withOpacity(0.5),
            width: 1,
          ),
          borderRadius: BorderRadius.circular(16),
          color: Colors.white,
          boxShadow: const [],
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
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  Icon(iconList[id % 4]),
                  Text("#${keywords[0]}#${keywords[1]}"),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
