import 'package:flutter/material.dart';

class SlideItem {
  final int userId;
  final int id;
  final String title;

  SlideItem({
    required this.userId,
    required this.id,
    required this.title,
  });
}

class CardWidget extends StatelessWidget {
  final int userId;
  final int id;
  final String title;

  const CardWidget({
    super.key,
    required this.userId,
    required this.id,
    required this.title,
  });

  @override
  Widget build(BuildContext context) {
    String displayedTitle =
        title.length > 6 ? "${title.substring(0, 6)}..." : title;

    return Card(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(displayedTitle, style: Theme.of(context).textTheme.titleLarge),
          ListTile(
            title: Text('User ID: $userId'),
          ),
          ListTile(
            title: Text('ID: $id'),
          ),
        ],
      ),
    );
  }
}
