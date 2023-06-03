import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/tabs/tab_home.dart';
import 'package:naemansan/tabs/tab_trail.dart';
import 'package:naemansan/tabs/tab_myrail.dart';
import 'package:naemansan/tabs/tab_mypage.dart';

class IndexScreen extends StatefulWidget {
  const IndexScreen({Key? key}) : super(key: key);
  @override
  _IndexScreenState createState() => _IndexScreenState();
}

class _IndexScreenState extends State<IndexScreen> {
  dynamic userInfo = '';
  static const storage = FlutterSecureStorage();
  @override
  void initState() {
    // TODO: implement initState
    getLoginStatus();
    super.initState();
    // WidgetsBinding.instance.addPostFrameCallback((_) {
    //   _asyncMethod();
    // });
  }

  // _asyncMethod() async {
  //   final accessToken = await storage.read(key: 'accessToken');
  //   print("??????;$accessToken ");
  //   if (accessToken == null) {
  //     goLogin();
  //   }
  // }

  Future<void> getLoginStatus() async {
    userInfo = await storage.read(key: 'login');
    print("userInfo 가 있냐고 $userInfo");

    if (userInfo == null) {
      goLogin();
    }
    setState(
      () {},
    );
  }

  goLogin() =>
      Navigator.pushNamedAndRemoveUntil(context, '/login', (route) => false);

  int currentIndex = 0;
  final List<Widget> tabs = [
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
        currentIndex: currentIndex,
        onTap: (index) {
          setState(() {
            currentIndex = index;
          });
        },
        items: [
          BottomNavigationBarItem(
            icon: Icon(
              currentIndex == 0 ? Icons.home : Icons.home_outlined,
            ),
            label: '홈',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              currentIndex == 1 ? Icons.forest : Icons.forest_outlined,
            ),
            label: '산책로',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              currentIndex == 2 ? Icons.park : Icons.park_outlined,
            ),
            label: '나만의산책로',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              currentIndex == 3 ? Icons.person : Icons.person_outlined,
            ),
            label: '마이페이지',
          ),
        ],
      ),
      body: tabs[currentIndex],
    );
  }
}
