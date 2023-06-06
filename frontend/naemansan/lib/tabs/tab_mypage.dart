import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/screens/screen_index.dart';
import 'package:naemansan/services/mypage_api_service.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:naemansan/profile_tabs/profile_tab_edit.dart';
import 'package:naemansan/tabs/tab_myrail.dart';

class Mypage extends StatefulWidget {
  const Mypage({Key? key}) : super(key: key);

  @override
  State<Mypage> createState() => _MypageState();
}

class _MypageState extends State<Mypage> {
  late Future<Map<String, dynamic>?> user;
  static const storage = FlutterSecureStorage();
  dynamic userInfo = '';

  // Fetch user info
  Future<Map<String, dynamic>?> fetchUserInfo() async {
    ProfileApiService apiService = ProfileApiService();
    return await apiService.getUserInfo();
  }

  @override
  void initState() {
    super.initState();
    user = fetchUserInfo();

    WidgetsBinding.instance.addPostFrameCallback((_) {
      checkUserState();
    });
  }

  Future<void> logout() async {
    await deleteTokens();
    await storage.delete(key: 'login');
    goLogin();
  }

  checkUserState() async {
    userInfo = await storage.read(key: 'login');
    if (userInfo == null) {
      print('로그인 페이지로 이동');
      goLogin();
    } else {
      print('로그인 중');
    }
  }

  goLogin() {
    Navigator.pushNamedAndRemoveUntil(context, '/login', (route) => false);
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
                  fontSize: 21,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const Expanded(child: SizedBox(width: 30)),
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
      body: SingleChildScrollView(
        child: Column(
          children: [
            Align(
              alignment: Alignment.topRight,
              child: IconButton(
                padding: const EdgeInsets.only(top: 0, right: 2.5),
                icon: const Icon(Icons.edit, color: Colors.black),
                onPressed: () async {
                  // 프로필 수정 페이지로 이동
                  await Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const Editpage(),
                    ),
                  );
                  // 정보 수정 후 다시 가져오기
                  setState(() {
                    user = fetchUserInfo();
                  });
                },
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 50),
              child: FutureBuilder<Map<String, dynamic>?>(
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

                      return Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          CircleAvatar(
                            radius: 50,
                            backgroundImage: NetworkImage(imageUrl),
                          ),
                          const SizedBox(height: 16),
                          Text(
                            userData?['name'] ?? 'No Name',
                            style: const TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            userData?['introduction'] ?? 'No Introduction',
                            style: const TextStyle(fontSize: 16),
                          ),
                          const SizedBox(height: 16),
                          const Divider(),
                          const SizedBox(height: 16),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              Column(
                                children: [
                                  const Text(
                                    '팔로워',
                                    style: TextStyle(fontSize: 16),
                                  ),
                                  TextButton(
                                    onPressed: () {},
                                    child: Text(
                                      userData?['follower_cnt'].toString() ??
                                          '0',
                                      style: const TextStyle(
                                        color: Colors.black,
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                              Column(
                                children: [
                                  const Text(
                                    '팔로잉',
                                    style: TextStyle(fontSize: 16),
                                  ),
                                  const SizedBox(height: 8),
                                  TextButton(
                                    onPressed: () {},
                                    child: Text(
                                      userData?['following_cnt'].toString() ??
                                          '0',
                                      style: const TextStyle(
                                        color: Colors.black,
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ],
                          ),
                          const SizedBox(height: 16),
                          const Divider(),
                          const SizedBox(height: 16),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              Column(
                                children: [
                                  const Text(
                                    '좋아요한 산책로',
                                    style: TextStyle(fontSize: 16),
                                  ),
                                  const SizedBox(height: 8),
                                  TextButton(
                                    onPressed: () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder: (context) =>
                                              const Myrail(initialTabIndex: 1),
                                        ),
                                      );
                                    },
                                    child: Text(
                                      '${userData?['like_cnt'] ?? 0}',
                                      style: const TextStyle(
                                        color: Colors.black,
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                              Column(
                                children: [
                                  const Text(
                                    '작성한 후기',
                                    style: TextStyle(fontSize: 16),
                                  ),
                                  const SizedBox(height: 8),
                                  TextButton(
                                    onPressed: () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder: (context) =>
                                              const Myrail(initialTabIndex: 3),
                                        ),
                                      );
                                    },
                                    child: Text(
                                      '${userData?['comment_cnt'] ?? 0}',
                                      style: const TextStyle(
                                        color: Colors.black,
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                              Column(
                                children: [
                                  const Text(
                                    '획득한 뱃지',
                                    style: TextStyle(fontSize: 16),
                                  ),
                                  const SizedBox(height: 8),
                                  TextButton(
                                    onPressed: () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder: (context) => const Badge(),
                                        ),
                                      );
                                    },
                                    child: Text(
                                      '${userData?['badge_cnt'] ?? 0}',
                                      style: const TextStyle(
                                        color: Colors.black,
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ],
                      );
                    } else {
                      return const Text('No user data available.');
                    }
                  }
                },
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: BottomAppBar(
        child: SizedBox(
          height: 50,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: logout,
                child: const Text('Logout'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  // Delete tokens on logout
  Future<void> deleteTokens() async {
    await storage.delete(key: 'accessToken');
    await storage.delete(key: 'refreshToken');
    print("삭제 진행함?");

    final prefs = await SharedPreferences.getInstance();
    prefs.setBool('isLogged', false);
  }
}
