import 'package:flutter/material.dart';
import 'package:naemansan/screens/course_detail_byID.dart';

class SlideItem {
  final int id;
  final String title;
  final String location;
  final double length;
  final int likes;
  final List<String> keywords;
  final String created_date;

  SlideItem({
    required this.id,
    required this.title,
    required this.location,
    required this.length,
    required this.likes,
    required this.keywords,
    required this.created_date,
  });
}

class CardWidget extends StatelessWidget {
  final int id;
  final String title;
  final String location;
  final double length;
  final int likes;
  final List<String> keywords;
  final String created_date;

  const CardWidget({
    Key? key,
    required this.id,
    required this.title,
    required this.location,
    required this.length,
    required this.likes,
    required this.keywords,
    required this.created_date,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    String displayedTitle =
        title.length > 10 ? "${title.substring(0, 10)}..." : title;

    String displayedLocation = location.replaceAll("서울특별시", "");
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
            builder: (context) => CourseDetailbyID(
              id: id,
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
        ),
        child: Padding(
          padding: const EdgeInsets.all(6.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Text(
                displayedTitle,
                style: const TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.w700,
                ),
              ),
              Text(
                displayedLocation,
                style: const TextStyle(
                  fontSize: 12,
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
