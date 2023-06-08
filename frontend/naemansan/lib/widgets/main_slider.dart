import 'package:flutter/material.dart';

class MainSlider extends StatefulWidget {
  final String title;
  final Widget sliderWidget;

  const MainSlider({
    Key? key,
    required this.title,
    required this.sliderWidget,
  }) : super(key: key);

  @override
  _MainSliderState createState() => _MainSliderState();
}

class _MainSliderState extends State<MainSlider> {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SizedBox(height: 10),
        SizedBox(
          height: 170,
          child: widget.sliderWidget,
        ),
        const SizedBox(height: 20),
      ],
    );
  }
}
