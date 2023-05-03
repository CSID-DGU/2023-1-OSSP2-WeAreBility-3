import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        automaticallyImplyLeading: false, // 앱바의 뒤로가기 버튼을 없애기 위해 false로 설정
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        actions: [
          IconButton(
            onPressed: () {},
            icon: const Icon(Icons.notifications_none),
          )
        ],
        title: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          children: const [
            Padding(
              padding: EdgeInsets.only(left: 20.0),
              child: Text(
                '내만산',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            Icon(
              Icons.forest_outlined,
              color: Colors.green,
            ),
          ],
        ),
      ),
    );
  }
}
