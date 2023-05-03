import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_home.dart';
import 'package:naemansan/tabs/tab_trail.dart';
import 'package:naemansan/tabs/tab_myrail.dart';
import 'package:naemansan/tabs/tab_mypage.dart';

class IndexScreen extends StatefulWidget {
  const IndexScreen({super.key});

  @override
  _IndexScreenState createState() => _IndexScreenState();
}

class _IndexScreenState extends State<IndexScreen> {
  int _currentIndex = 0;
  final List<Widget> _tabs = [
    const Home(),
    const Trail(),
    const Myrail(),
    const Mypage(),
  ];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 1,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
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
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        iconSize: 30,
        selectedItemColor: Colors.green,
        unselectedItemColor: Colors.black,
        selectedLabelStyle: const TextStyle(fontSize: 12),
        currentIndex: _currentIndex,
        onTap: (index) {
          setState(() {
            _currentIndex = index;
          });
        },
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home_outlined), label: '홈'),
          BottomNavigationBarItem(
              icon: Icon(Icons.forest_outlined), label: '산책로'),
          BottomNavigationBarItem(
              icon: Icon(Icons.park_outlined), label: '나만의산책로'),
          BottomNavigationBarItem(
              icon: Icon(Icons.person_outlined), label: '마이페이지'),
        ],
      ),
      body: _tabs[_currentIndex],
    );
  }
}
