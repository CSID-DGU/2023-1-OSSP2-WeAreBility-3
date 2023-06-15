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
    """
안녕하세요!\n
\n
내가 만드는 산책로, 내만산입니다.\n
\n
내만산은 양방향 산책로 공유 서비스 지원함으로서 ㄱ\n
개인만의 산책로를 알릴 수 있는 플랫폼입니다.\n
\n
[title]나만의 산책로 생성\n
\n
나만의 산책로를 만들어봐요!\n
나만의 산책로 -> 상단 + 아이콘을 클릭하면 나만의 산책로를 만들 수 있답니다.\n
직접 산책해야 길을 생성할 수 있습니다!\n
\n
[title]모두의 산책로\n
\n
나만의 산책로를 공유해봐요!\n
나만의 산책로를 공유함으로서 나만의 산책로를 더 널리알릴 수 있고 \n
다른이의 산책로를 이용함으로서 다양한 산책로들을 알아갈 수 있어요! 
\n
[title]맞춤형 산책로 추천\n
\n
공개등록된 산책로를 위치, 키워드 별로 추천받을 수 있습니다.\n
이를 통해 더 다양한 산책로들을 구경하고 이용하는\n
시간을 가져봐요 :)\n""",
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
