import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_home.dart';
import 'package:naemansan/tabs/tab_trail.dart';
import 'package:naemansan/tabs/tab_myrail.dart';
import 'package:naemansan/tabs/tab_mypage.dart';

//화면 확인용
//import 'package:naemansan/profile_tabs/profile_tab_settings.dart';
//import 'package:naemansan/profile_tabs/profile_tab_badges.dart';
//import 'package:naemansan/profile_tabs/profile_tab_reviews.dart';
//import 'package:naemansan/profile_tabs/profile_tab_edit.dart';
//import 'package:naemansan/profile_tabs/view_profile.dart';

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
        items: [
          BottomNavigationBarItem(
            icon: Icon(
              _currentIndex == 0 ? Icons.home : Icons.home_outlined,
            ),
            label: '홈',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              _currentIndex == 1 ? Icons.forest : Icons.forest_outlined,
            ),
            label: '산책로',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              _currentIndex == 2 ? Icons.park : Icons.park_outlined,
            ),
            label: '나만의산책로',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              _currentIndex == 3 ? Icons.person : Icons.person_outlined,
            ),
            label: '마이페이지',
          ),
        ],
      ),
      body: _tabs[_currentIndex],
    );
  }
}
