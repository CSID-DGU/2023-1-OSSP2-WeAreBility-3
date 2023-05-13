import 'package:flutter/material.dart';

class HorizontalSlider extends StatelessWidget {
  final List<Widget> items;

  const HorizontalSlider({super.key, required this.items});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 140.0,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        itemCount: items.length,
        itemBuilder: (context, index) {
          return Container(
            width: 120.0,
            height: 140.0,
            margin: const EdgeInsets.symmetric(horizontal: 8.0),
            child: items[index],
          );
        },
      ),
    );
  }
}
