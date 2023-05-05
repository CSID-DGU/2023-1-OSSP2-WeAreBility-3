import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';

class BannerSwiper extends StatelessWidget {
  final int _currentPage = 0;
  final List<String> images = [
    "https://via.placeholder.com/300/09f/fff.png",
    "https://via.placeholder.com/300/0c5/fff.png",
    "https://via.placeholder.com/300/06f/fff.png",
  ];

  BannerSwiper({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 200,
      child: CarouselSlider.builder(
        itemCount: images.length,
        itemBuilder: (BuildContext context, int index, int realIndex) {
          final imageUrl = images[index];
          return Image.network(
            imageUrl,
            fit: BoxFit.cover,
          );
        },
        options: CarouselOptions(
          autoPlay: true,
          enlargeCenterPage: true,
        ),
      ),
    );
  }
}
