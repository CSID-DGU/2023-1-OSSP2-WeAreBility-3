import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_home.dart';
import 'package:naemansan/tabs/tab_trail.dart';
import 'package:naemansan/tabs/tab_myrail.dart';
import 'package:naemansan/tabs/tab_mypage.dart';
import 'package:shared_preferences/shared_preferences.dart';

class IndexScreen extends StatefulWidget {
  const IndexScreen({Key? key}) : super(key: key);
  @override
  _IndexScreenState createState() => _IndexScreenState();
}

class _IndexScreenState extends State<IndexScreen> {
  bool isLogged_local = false;

  Future<void> _checkLoginStatus() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      isLogged_local = prefs.getBool('isLogged') ?? false;
      _checkLoginStatus();
      print("지금 screen_index.dart 가 파악하는 로그인 상태는$isLogged_local");
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  int _currentIndex = 0;
  final List<Widget> _tabs = [
    const Home(),
    const Trail(), //임시
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
