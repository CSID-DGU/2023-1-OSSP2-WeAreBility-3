import 'package:flutter/material.dart';

class Trail extends StatelessWidget {
  final String title;
  final String startpoint;
  final double distance;
  final List<String>? CourseKeyWord;
  final int likeCnt, userCnt;
  final bool isLiked;

  const Trail({
    Key? key,
    required this.title,
    required this.startpoint,
    required this.distance,
    this.CourseKeyWord,
    required this.likeCnt,
    required this.userCnt,
    required this.isLiked,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 10.0),
      child: Container(
        height: 130.0,
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(
            color: Colors.grey.withOpacity(0.5),
            width: 1,
          ),
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(
          children: [
            const SizedBox(
              width: 100.0,
              child: Icon(Icons.image),
            ),
            const SizedBox(width: 4.0),
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    title,
                    style: const TextStyle(
                      fontSize: 18.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4.0),
                  Text(
                    startpoint,
                    style: const TextStyle(
                      fontSize: 12.0,
                    ),
                  ),
                  Text(
                    '$distance km',
                    style: const TextStyle(
                      fontSize: 12.0,
                    ),
                  ),
                  if (CourseKeyWord != null)
                    Wrap(
                      children: CourseKeyWord!
                          .map((word) => Text(
                                '#$word ',
                                style: const TextStyle(
                                  fontSize: 12.0,
                                  color: Colors.black,
                                ),
                              ))
                          .toList(),
                    ),
                ],
              ),
            ),
            Column(
              children: [
                Row(
                  children: [
                    const SizedBox(width: 8.0, height: 4.0),
                    IconButton(
                      icon: const Icon(Icons.arrow_forward_ios_outlined),
                      onPressed: () {}, //산책로 세부 페이지로 이동
                    ),
                  ],
                ),
                const SizedBox(height: 15),
                Row(
                  children: [
                    Icon(
                      isLiked ? Icons.favorite : Icons.favorite_border_outlined,
                      color: isLiked ? Colors.red : null,
                    ),
                    Text(
                      '$likeCnt',
                      style: const TextStyle(
                        fontSize: 16.0,
                      ),
                    ),
                  ],
                ),
                Row(
                  children: [
                    const Icon(Icons.person_outline),
                    Text(
                      '$userCnt',
                      style: const TextStyle(
                        fontSize: 16.0,
                      ),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(width: 4.0),
          ],
        ),
      ),
    );
  }
}
