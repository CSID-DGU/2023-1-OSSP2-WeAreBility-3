import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';

class BannerSwiper extends StatelessWidget {
  final List<String> images = [
    "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Aspect_ratio_-_16x9.svg/2560px-Aspect_ratio_-_16x9.svg.png",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Aspect_ratio_-_16x9.svg/2560px-Aspect_ratio_-_16x9.svg.png",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Aspect_ratio_-_16x9.svg/2560px-Aspect_ratio_-_16x9.svg.png",
  ];

  final List<String> captions = [
    "산책을 즐기는 5가지 방법",
    "내만산 이용하는 방법",
    "산책시 유의점",
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

                return GestureDetector(
                  onTap: () {
                    // Handle slide click here
                    // You can navigate to the corresponding page using Navigator
                    print("Clicked on slide $index");
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
