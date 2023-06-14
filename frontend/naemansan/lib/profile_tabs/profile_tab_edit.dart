import 'package:flutter/material.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/services/mypage_api_service.dart';
import 'package:naemansan/profile_tabs/profile_introduction_edit.dart';
import 'package:naemansan/profile_tabs/profile_name_edit.dart';
import 'package:naemansan/profile_tabs/profile_image_edit.dart';

class Editpage extends StatefulWidget {
  const Editpage({Key? key}) : super(key: key);
  @override
  State<Editpage> createState() => _EditpageState();
}

class _EditpageState extends State<Editpage> {
  late Future<Map<String, dynamic>?> user;
  static const storage = FlutterSecureStorage();
  dynamic userInfo = '';
  late String newIntro;
  late String newName;

  // Fetch user info
  Future<Map<String, dynamic>?> fetchUserInfo() async {
    ProfileApiService apiService = ProfileApiService();
    return await apiService.getUserInfo();
  }

  Future<void> saveChanges() async {
    final profileApiService = ProfileApiService();
    final response = await profileApiService.putRequest('user', {
      'name': newName,
      'introduction': newIntro,
    });
    fetchUserInfo(); // 사용자 정보 다시 불러오기
  }

  @override
  void initState() {
    super.initState();
    user = fetchUserInfo();

    user.then((userInfo) {
      if (userInfo != null) {
        newIntro = userInfo['introduction'] ?? '';
        newName = userInfo['name'] ?? '';
        setState(() {});
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        titleSpacing: 0,
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back_ios_outlined,
            color: Colors.black,
          ),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        title: Row(
          children: [
            const Padding(
              padding: EdgeInsets.only(left: 5.0),
              child: Text(
                '프로필 수정',
                style: TextStyle(
                  fontSize: 21,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const Spacer(),
            TextButton(
              onPressed: () {
                saveChanges();

                Navigator.of(context).pushAndRemoveUntil(
                  MaterialPageRoute(
                      builder: (context) => const IndexScreen(index: 3)),
                  (route) => false,
                );
              },
              child: const Text(
                '완료',
                style: TextStyle(
                    color: Colors.black,
                    fontSize: 20.0,
                    fontWeight: FontWeight.w600),
              ),
            ),
            const SizedBox(width: 6),
          ],
        ),
      ),
      body: FutureBuilder<Map<String, dynamic>?>(
        future: user,
        builder: (BuildContext context,
            AsyncSnapshot<Map<String, dynamic>?> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const CircularProgressIndicator();
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            if (snapshot.hasData) {
              Map<String, dynamic>? userData = snapshot.data;
              String imageFileName =
                  userData?['image_path'] ?? '0_default_image.png';
              String imageUrl =
                  'https://ossp.dcs-hyungjoon.com/image?uuid=$imageFileName';
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Stack(
                      children: [
                        CircleAvatar(
                          radius: 50,
                          backgroundImage: NetworkImage(imageUrl),
                        ),
                        Positioned(
                          bottom: -10,
                          right: -10,
                          child: IconButton(
                            icon: const Icon(Icons.edit),
                            onPressed: () async {
                              final result = await Navigator.of(context).push(
                                MaterialPageRoute(
                                  builder: (BuildContext context) =>
                                      ProfileImageEditPage(userInfo: userData),
                                ),
                              );
                              if (result == true) {
                                setState(() {
                                  user = fetchUserInfo(); // 사용자 정보 다시 불러오기
                                });
                              }
                            },
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 25),
                      child: Column(
                        children: [
                          const Divider(
                            thickness: 1,
                          ),
                          Row(
                            children: [
                              Expanded(
                                child: Text(
                                  userData?['name'] ?? 'No Name',
                                  style: const TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.bold,
                                  ),
                                  textAlign: TextAlign.center, // 텍스트 가운데 정렬
                                ),
                              ),
                              IconButton(
                                icon: const Icon(Icons.edit),
                                onPressed: () {
                                  // 이름 수정 부분
                                  Navigator.of(context)
                                      .push(
                                    MaterialPageRoute(
                                      builder: (BuildContext context) =>
                                          ProfileNameEditPage(
                                              userInfo: userData),
                                    ),
                                  )
                                      .then((value) {
                                    if (value != null) {
                                      newName = value; // 수정된 값을 대입
                                    }
                                    setState(() {});
                                  });
                                },
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 20),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 25),
                      child: Column(
                        children: [
                          const Divider(
                            thickness: 1,
                          ),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Expanded(
                                child: Padding(
                                  padding: const EdgeInsets.only(top: 8),
                                  child: Text(
                                    userData?['introduction'] ??
                                        'No Introduction',
                                    style: const TextStyle(
                                      fontSize: 16,
                                      fontWeight: FontWeight.bold,
                                    ),
                                    textAlign: TextAlign.center, // 텍스트 가운데 정렬
                                  ),
                                ),
                              ),
                              IconButton(
                                icon: const Icon(Icons.edit),
                                onPressed: () {
                                  // Introduction 수정 부분
                                  Navigator.of(context)
                                      .push(
                                    MaterialPageRoute(
                                      builder: (BuildContext context) =>
                                          ProfileIntroEditPage(
                                              userInfo: userData),
                                    ),
                                  )
                                      .then((value) {
                                    newIntro = value;
                                    setState(() {});
                                  });
                                },
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              );
            } else {
              return const Text('No user data available.');
            }
          }
        },
      ),
    );
  }
}
