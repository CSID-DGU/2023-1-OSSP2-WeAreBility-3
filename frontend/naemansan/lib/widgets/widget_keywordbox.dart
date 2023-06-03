import 'package:flutter/material.dart';

class KeyWordBoxWidget extends StatelessWidget {
  final String tag;

  const KeyWordBoxWidget({
    Key? key,
    required this.tag,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 0.0, vertical: 0.0),
      child: Text(tag),
    );
  }
}
