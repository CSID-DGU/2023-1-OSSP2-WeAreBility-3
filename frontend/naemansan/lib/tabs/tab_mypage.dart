import 'package:flutter/material.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Mypage extends StatelessWidget {
  const Mypage({super.key});

  signOut(BuildContext context) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      prefs.setBool('isLogged', false);
      Navigator.pushNamedAndRemoveUntil(context, '/', (route) => false);
      await UserApi.instance.unlink();
      await UserApi.instance.logout();
    } catch (error) {
      print('카카오계정으로 로그인 아웃 실패 $error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        automaticallyImplyLeading: false,
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            IconButton(
              padding: EdgeInsets.zero,
              constraints: const BoxConstraints.tightFor(width: 20),
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const IndexScreen(),
                  ),
                );
              },
            ),
            const Padding(
              padding: EdgeInsets.only(left: 20),
              child: Text(
                '마이페이지',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const Expanded(child: SizedBox(width: 30)), // 여백 추가
            IconButton(
              padding: const EdgeInsets.only(left: 25),
              icon: const Icon(
                Icons.settings,
                color: Colors.black,
              ),
              onPressed: () {},
            ),
          ],
        ),
      ),
      body: Column(
        children: [
          Row(
            children: [
              const SizedBox(
                width: 363,
              ),
              IconButton(
                padding: const EdgeInsets.only(
                    top: 10, right: 2.5), // 위에 톱니바퀴랑 위치 통일하기
                icon: const Icon(Icons.edit, color: Colors.black),
                onPressed: () {
                  // 프로필 수정 페이지로 이동
                },
              ),
            ],
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Column(
                children: [
                  Container(
                    width: 120,
                    height: 120,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      border: Border.all(
                        color: Colors.blueGrey,
                        width: 1,
                      ),
                    ),
                    child: const CircleAvatar(
                      radius: 65,
                      backgroundColor: Colors.white70, // 배경색
                      //레이아웃 확인용
                      child: Text(
                        '프로필 사진',
                        style: TextStyle(fontSize: 32),
                      ),
                    ),
                  ),
                  Row(
                    children: const [
                      Text(
                        '이름',
                        style: TextStyle(
                          fontSize: 30,
                          color: Colors.black,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 50.0, vertical: 5),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: const [
                Expanded(
                  child: Text(
                    '회색 빛의 서울시의 한걸음 한걸음 모여 밝은 빛을 발견할 수 있는 도심 속 산책자입니다',
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 18, color: Colors.black),
                    softWrap: true,
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 10,
          ),
          Container(
            height: 0.3,
            width: 360,
            color: Colors.grey,
          ),
          Row(
            children: [
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: const [
                    Text(
                      '팔로워',
                      style: TextStyle(fontSize: 18, color: Colors.black),
                    ),
                    SizedBox(
                      height: 10,
                    ),
                    Text(
                      '팔로워수',
                      style: TextStyle(
                        fontSize: 30,
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                width: 0.3,
                height: 100,
                child: Container(
                  color: Colors.grey,
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: const [
                    Text(
                      '팔로잉',
                      style: TextStyle(fontSize: 18, color: Colors.black),
                    ),
                    SizedBox(
                      height: 10,
                    ),
                    Text(
                      '팔로잉 수',
                      style: TextStyle(
                        fontSize: 30,
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(
                height: 10,
              ),
            ],
          ),
          const SizedBox(
            height: 10,
          ),
          Container(
            height: 0.4,
            width: 360,
            color: Colors.grey,
          ),
          const SizedBox(
            height: 10,
          ),
          Row(
            children: [
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: const [
                    Text(
                      '이용한 산책로',
                      style: TextStyle(fontSize: 15, color: Colors.black),
                    ),
                    SizedBox(
                      height: 10,
                    ),
                    Text(
                      '0',
                      style: TextStyle(
                        fontSize: 20,
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                width: 0.3,
                height: 70,
                child: Container(
                  color: Colors.grey,
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: const [
                    Text(
                      '획득한 뱃지',
                      style: TextStyle(fontSize: 15, color: Colors.black),
                    ),
                    SizedBox(
                      height: 10,
                    ),
                    Text(
                      '0',
                      style: TextStyle(
                        fontSize: 20,
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                width: 0.3,
                height: 70,
                child: Container(
                  color: Colors.grey,
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: const [
                    Text(
                      '작성한 후기',
                      style: TextStyle(fontSize: 15, color: Colors.black),
                    ),
                    SizedBox(
                      height: 10,
                    ),
                    Text(
                      '0',
                      style: TextStyle(
                        fontSize: 20,
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(
            height: 10,
          ),
          Container(
            height: 0.4,
            width: 360,
            color: Colors.grey,
          ),
          Padding(
            padding: const EdgeInsets.only(
              left: 35,
              top: 15,
            ),
            child: Row(
              children: const [
                Text(
                  '0월 산책목표', //월 정보 불러오기
                  style: TextStyle(fontWeight: FontWeight.w500),
                ),
              ],
            ),
          ),
          //목표 달성률 이미지 구현

          TextButton(
            onPressed: () => signOut(context),
            child: const Text(
              '로그아웃',
              style: TextStyle(color: Colors.grey),
            ),
          )
        ],
      ),
    );
  }
}
