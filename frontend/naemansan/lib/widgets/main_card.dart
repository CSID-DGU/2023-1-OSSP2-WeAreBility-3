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
    return Card(
      child: Column(
        children: [
          ListTile(
            leading: const Icon(Icons.person),
            title: Text('User ID: $userId'),
          ),
          ListTile(
            leading: const Icon(Icons.info),
            title: Text('ID: $id'),
          ),
          ListTile(
            leading: const Icon(Icons.title),
            title: Text('Title: $title'),
          ),
        ],
      ),
    );
  }
}
