import 'package:flutter/material.dart';
import 'package:naemansan/screens/screen_index.dart';

/*
import 'package:naemansan/profile_tabs/badges.dart';
import 'package:naemansan/profile_tabs/my_reviews.dart';
import 'package:naemansan/profile_tabs/profile_edit.dart';
import 'package:naemansan/profile_tabs/settings.dart';
*/

class View extends StatelessWidget {
  const View({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            IconButton(
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                //arrow 아이콘 클릭 시 홈 화면으로 이동 (홈 화면에 화살표 생김)
                false;
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const IndexScreen(),
                  ),
                );
              },
            ),
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: const [
                  Text(
                    '000님의 프로필', //추가 작업 필요
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  SizedBox(width: 8),
                ],
              ),
            ),
            const Spacer(),
            IconButton(
              icon: const Icon(
                Icons.person_add_alt_1,
                color: Colors.black,
              ),
              onPressed: () {},
            ),
          ],
        ),
      ),
    );
  }
}
