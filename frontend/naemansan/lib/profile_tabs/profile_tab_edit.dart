import 'package:flutter/material.dart';
import 'package:naemansan/tabs/tab_mypage.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/services/login_api_service.dart';

class Editpage extends StatefulWidget {
  const Editpage({Key? key}) : super(key: key);

  @override
  State<Editpage> createState() => _EditpageState();
}

class _EditpageState extends State<Editpage> {
  late Future<Map<String, dynamic>?> user;
  static const storage = FlutterSecureStorage();
  dynamic userInfo = '';

  // Fetch user info
  Future<Map<String, dynamic>?> fetchUserInfo() async {
    ApiService apiService = ApiService();
    return await apiService.getUserInfo();
  }

  @override
  void initState() {
    super.initState();
    user = fetchUserInfo();
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
                false;
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
            InkWell(
              onTap: () {
                // 버튼을 클릭 시 프로필 수정 완료
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
                  //crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    CircleAvatar(
                      radius: 50,
                      backgroundImage: NetworkImage(imageUrl),
                    ),
                    const SizedBox(height: 20),
                    const Padding(
                      padding: EdgeInsets.symmetric(horizontal: 25),
                      child: Divider(
                        thickness: 1,
                      ),
                    ),
                    Text(
                      userData?['name'] ?? 'No Name',
                      style: const TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 20),
                    const Padding(
                      padding: EdgeInsets.symmetric(horizontal: 25),
                      child: Divider(
                        thickness: 1,
                      ),
                    ),
                    Text(
                      userData?['introduction'] ?? 'No Introduction',
                      style: const TextStyle(fontSize: 16),
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
