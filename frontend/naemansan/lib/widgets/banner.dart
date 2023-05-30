import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:naemansan/screens/Home/banner_detail_screen.dart';

class BannerSwiper extends StatelessWidget {
  final List<String> images = [
    "https://i.pinimg.com/originals/89/2c/bc/892cbcd3d9d1982f3966acf2e41650d4.jpg",
    "https://mblogthumb-phinf.pstatic.net/MjAxNzExMjJfMjUg/MDAxNTExMzI3NDIxODY3.1xiNaSwKDa6ETU8OhwUkofIWry9BGxAkfZJTtMXQOxkg.T2GO8rkb1FApzH2HOj9EoXnJPILRd6luo37JJqc4Lcsg.JPEG.knicjin/20171122-018.jpg?type=w800",
    "https://img.freepik.com/premium-photo/detail-of-the-milky-way-with-photographers-panoramic-view-ultrawide-16-9-resolution_38810-698.jpg",
  ];

  final List<String> captions = [
    "산책을 즐기는 5가지 방법",
    "내만산 이용하는 방법",
    "산책시 유의점",
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
          Container(
            child: CarouselSlider(
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
                                      fontSize: 24,
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
          ),
        ],
      ),
    );
  }
}
