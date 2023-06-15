import 'package:flutter/material.dart';
// import 'package:lottie/lottie.dart';

class BannerDetailScreen extends StatelessWidget {
  final String caption;
  final String content;

  const BannerDetailScreen({
    Key? key,
    required this.caption,
    required this.content,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: const Text("내가 만드는 산책로, 내만산"),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 30),
              Center(
                child: Text(
                  caption,
                  style: const TextStyle(
                    fontSize: 21,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              // Lottie.asset('assets/lottie/sample.json'),

              const SizedBox(height: 30),
              Image.network(
                "https://velog.velcdn.com/images/seochan99/post/395ade8c-409f-49a8-9725-a37c6ed1f894/image.png",
                fit: BoxFit.cover,
              ),
              const SizedBox(height: 50),
              RichText(
                text: TextSpan(
                  style: const TextStyle(fontSize: 16, color: Colors.black),
                  children: _buildContentWithTitles(),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  List<TextSpan> _buildContentWithTitles() {
    final List<TextSpan> textSpans = [];
    final List<String> lines = content.split('\n');
    for (final line in lines) {
      if (line.startsWith('[title]')) {
        final title = line.substring(7);
        textSpans.add(
          TextSpan(
            text: '🎯 $title\n', // Include line breaks
            style: const TextStyle(
              fontSize: 23,
              fontWeight: FontWeight.bold,
            ),
          ),
        );
      } else {
        textSpans.add(TextSpan(text: '$line\n')); // Include line breaks
      }
    }
    return textSpans;
  }
}
