import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';

class BannerSwiper extends StatelessWidget {
  final int _currentPage = 0;
  final List<String> images = [
    "https://www.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=46163&fileTy=MEDIA&fileNo=1",
    "https://www.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=46163&fileTy=MEDIA&fileNo=1",
    "https://previews.123rf.com/images/doraclub/doraclub1308/doraclub130800051/21489706-%EA%B3%B5%EC%9B%90-%EC%82%B0%EC%B1%85%EB%A1%9C.jpg",
  ];

  BannerSwiper({
    super.key,
  });

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

                return Stack(
                  children: [
                    Image.network(
                      imageUrl,
                      fit: BoxFit.cover,
                      width: double.infinity,
                    ),
                    Positioned(
                      bottom: 16,
                      right: 16,
                      child: Text(
                        "${index + 1}/${images.length}",
                        style: const TextStyle(
                          fontSize: 16,
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                );
              }).toList(),
              options: CarouselOptions(
                autoPlay: true,
                enlargeCenterPage: false,
              ),
            ),
          ),
          Positioned.fill(
            child: Container(
              color: Colors.black.withOpacity(0.4),
              child: Padding(
                padding: const EdgeInsets.only(left: 30),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: const [
                    Text(
                      "산책을 즐기는 5가지 방법",
                      style: TextStyle(
                        fontSize: 24,
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(
                      height: 20,
                    ),
                    Text(
                      "더 알아보기 >",
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.white,
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
    );
  }
}
