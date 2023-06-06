import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_mypage.dart';
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
  }

  @override
  void initState() {
    super.initState();
    user = fetchUserInfo();
    newIntro = '';
    newName = '';
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
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                //arrow 아이콘 클릭 시 마이페이지 화면으로 이동
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (BuildContext context) => const Mypage(),
                  ),
                );
              },
            ),
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: const [
                  Text(
                    '프로필 수정',
                    style: TextStyle(
                      fontSize: 21,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  SizedBox(width: 8),
                ],
              ),
            ),
            const Spacer(),
            TextButton(
              // ----------------------------------------------------- 수정해
              onPressed: () {
                saveChanges();
                Navigator.of(context).pop();
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
            //일단 이름, 소개, 사진 각각 완료버튼 누르면 바로 수정되는걸로 -> 이름이랑 소개 동시에 저장되게 수정중
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
                      alignment: Alignment.bottomRight,
                      children: [
                        CircleAvatar(
                          radius: 50,
                          backgroundImage: NetworkImage(imageUrl),
                        ),
                        IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () {
                            // 프로필 사진 수정 활성화
                            Navigator.of(context)
                                .push(
                              MaterialPageRoute(
                                builder: (BuildContext context) =>
                                    ProfileImageEditPage(userInfo: userData),
                              ),
                            )
                                .then((value) {
                              if (value == true) {
                                setState(() {
                                  user = fetchUserInfo(); // 사용자 정보 다시 불러오기
                                });
                              }
                            });
                          },
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),
                    const Padding(
                      padding: EdgeInsets.symmetric(horizontal: 25),
                      child: Divider(
                        thickness: 1,
                      ),
                    ),
                    //이름 수정 부분
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          userData?['name'] ?? 'No Name',
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () {
                            // name을 수정할 수 있는 화면
                            Navigator.of(context)
                                .push(
                              MaterialPageRoute(
                                builder: (BuildContext context) =>
                                    ProfileNameEditPage(userInfo: userData),
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
                    const SizedBox(height: 20),
                    const Padding(
                      padding: EdgeInsets.symmetric(horizontal: 25),
                      child: Divider(
                        thickness: 1,
                      ),
                    ),
                    //introduction 수정 부분
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          userData?['introduction'] ?? 'No Introduction',
                          style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () {
                            // introduction을 수정할 수 있는 화면
                            Navigator.of(context)
                                .push(
                              MaterialPageRoute(
                                builder: (BuildContext context) =>
                                    ProfileIntroEditPage(userInfo: userData),
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
