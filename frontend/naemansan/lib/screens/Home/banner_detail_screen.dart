import 'package:flutter/material.dart';

class BannerDetailScreen extends StatelessWidget {
  final String caption;
  final String content;

  const BannerDetailScreen({
    super.key,
    required this.caption,
    required this.content,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Text(caption),
      ),
      body: Container(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                caption,
                style: const TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              Text(
                content,
                style: const TextStyle(fontSize: 16),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
