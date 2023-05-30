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
  List<bool> keywordButtonStates = [
    true,
    false,
    false
  ]; // Initial button states

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          widget.title,
          style: const TextStyle(
            fontSize: 25,
            fontWeight: FontWeight.w800,
          ),
        ),
        const SizedBox(height: 20),
        if (widget.title.contains("키워드별"))
          Row(
            children: [
              _buildKeywordButton("한강", index: 0),
              _buildKeywordButton("숲", index: 1),
              _buildKeywordButton("공원", index: 2),
              // Add more keyword buttons as needed
            ],
          ),
        const SizedBox(height: 20),
        SizedBox(
          height: 170,
          child: widget.sliderWidget,
        ),
        const SizedBox(height: 20),
      ],
    );
  }

  Widget _buildKeywordButton(String keyword, {required int index}) {
    return Container(
      margin: const EdgeInsets.only(right: 10.0),
      child: ElevatedButton(
        onPressed: () {
          setState(() {
            // Update the button states based on the index
            for (int i = 0; i < keywordButtonStates.length; i++) {
              keywordButtonStates[i] = (i == index);
            }
          });
        },
        style: ButtonStyle(
          backgroundColor: MaterialStateProperty.all<Color>(
            keywordButtonStates[index]
                ? const Color.fromARGB(255, 26, 167, 85)
                : Colors.white,
          ),
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
            RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20),
            ),
          ),
        ),
        child: Text(
          keyword,
          style: TextStyle(
            color: keywordButtonStates[index] ? Colors.white : Colors.black,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
