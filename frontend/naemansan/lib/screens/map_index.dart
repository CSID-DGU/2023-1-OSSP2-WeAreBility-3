import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_home.dart';
import 'package:naemansan/tabs/tab_mypage.dart';
import 'package:naemansan/tabs/map.dart';

class MapIndex extends StatefulWidget {
  const MapIndex({super.key});

  @override
  _MapIndexState createState() => _MapIndexState();
}

class _MapIndexState extends State<MapIndex> {
  int _currentIndex = 0;
  final List<Widget> _tabs = [
    const Home(),
    const Map(),
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
              _currentIndex == 1
                  ? Icons.map
                  : Icons.map_outlined, //하트 아이콘 찾으면 변경 !!
            ),
            label: '나만의지도',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              _currentIndex == 2 ? Icons.person : Icons.person_outlined,
            ),
            label: '마이페이지',
          ),
        ],
      ),
      body: _tabs[_currentIndex],
    );
  }
}
