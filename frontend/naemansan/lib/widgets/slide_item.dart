import 'package:flutter/material.dart';

class SlideItem extends StatelessWidget {
  final IconData icon;
  final String text;

  const SlideItem({super.key, required this.icon, required this.text});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(icon, size: 40.0),
        const SizedBox(height: 8.0),
        Text(text),
      ],
    );
  }
}
