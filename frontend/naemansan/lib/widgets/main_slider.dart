import 'package:flutter/material.dart';

class MainSlider extends StatelessWidget {
  final String title;
  final Widget sliderWidget;

  const MainSlider({
    super.key,
    required this.title,
    required this.sliderWidget,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          title,
          style: const TextStyle(
            fontSize: 25,
            fontWeight: FontWeight.w800,
          ),
        ),
        const SizedBox(height: 20),
        SizedBox(
          height: 170,
          child: sliderWidget,
        ),
        const SizedBox(height: 20),
      ],
    );
  }
}
