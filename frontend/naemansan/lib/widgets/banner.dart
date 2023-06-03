import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:naemansan/screens/Home/banner_detail_screen.dart';

class BannerSwiper extends StatelessWidget {
  final List<String> images = [
    "https://i.pinimg.com/564x/91/d7/fb/91d7fb01a2c4ad831a07c147d8138237.jpg",
    "https://i.pinimg.com/564x/d7/4f/46/d74f46a5980f8cf768b58c81f7038181.jpg",
    "https://i.pinimg.com/564x/2b/bd/be/2bbdbe2c5ab8176c3eef8a59368367bd.jpg",
  ];

  final List<String> captions = [
    "내가 만든 산책로, 내만산이란?",
    "산책을 즐기는 5가지 방법",
    "내만산 이용시 유의할점",
  ];

  final List<String> contents = [
    "1. 룰루 랄라? \n2. 룰루룰ㄹ루 \n3.으얽?\n4. 집 가고싶다.. \n5.흐하..",
    "1. 룰루 랄라? \n2. 룰루룰ㄹ루 \n3.으얽?\n4. 집 가고싶다.. \n5.흐하..",
    "1. 룰루 랄라? \n2. 룰루룰ㄹ루 \n3.으얽?\n4. 집 가고싶다.. \n5.흐하..",
  ];

  BannerSwiper({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      child: Stack(
        children: [
          CarouselSlider(
            items: images.asMap().entries.map((entry) {
              final index = entry.key;
              final imageUrl = entry.value;
              final caption = captions[index];
              final content = contents[index];

              return GestureDetector(
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => BannerDetailScreen(
                        caption: caption,
                        content: content,
                      ),
                    ),
                  );
                },
                child: AspectRatio(
                  aspectRatio: 16 / 9,
                  child: Stack(
                    children: [
                      Image.network(
                        imageUrl,
                        fit: BoxFit.cover,
                      ),
                      Positioned.fill(
                        child: Container(
                          color: Colors.black.withOpacity(0.4),
                          child: Padding(
                            padding: const EdgeInsets.only(left: 30),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  caption,
                                  style: const TextStyle(
                                    fontSize: 21,
                                    color: Colors.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                const SizedBox(height: 20),
                                const Text(
                                  "더 알아보기 >",
                                  style: TextStyle(
                                    fontSize: 16,
                                    color: Color.fromRGBO(255, 255, 255, 1),
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              );
            }).toList(),
            options: CarouselOptions(
              autoPlay: true,
              enlargeCenterPage: false,
              viewportFraction: 1.0,
            ),
          ),
        ],
      ),
    );
  }
}
