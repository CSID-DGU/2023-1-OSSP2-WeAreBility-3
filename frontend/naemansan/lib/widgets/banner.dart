import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';

class BannerSwiper extends StatelessWidget {
  final int _currentPage = 0;
  final List<String> images = [
    "https://www.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=46163&fileTy=MEDIA&fileNo=1",
    "https://www.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=46163&fileTy=MEDIA&fileNo=1",
    "https://previews.123rf.com/images/doraclub/doraclub1308/doraclub130800051/21489706-%EA%B3%B5%EC%9B%90-%EC%82%B0%EC%B1%85%EB%A1%9C.jpg",
  ];

  BannerSwiper({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity, // 가로로 꽉 차게 하려면 width를 설정
      child: CarouselSlider(
        items: images.map((imageUrl) {
          return Image.network(
            imageUrl,
            fit: BoxFit.cover,
          );
        }).toList(),
        options: CarouselOptions(
          autoPlay: true,
          enlargeCenterPage: false, // 가로로 꽉 차게 하려면 false로 설정
        ),
      ),
    );
  }
}
