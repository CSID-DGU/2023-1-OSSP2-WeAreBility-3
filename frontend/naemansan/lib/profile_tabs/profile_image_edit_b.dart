import 'dart:io';
import 'package:flutter/material.dart';
import 'package:naemansan/services/mypage_api_service.dart';
import 'package:image_picker/image_picker.dart';

class ProfileImageEditPage extends StatefulWidget {
  final Map<String, dynamic>? userInfo;
  final Function(Map<String, dynamic> updatedUserInfo)? onProfileUpdated;

  const ProfileImageEditPage({
    Key? key,
    required this.userInfo,
    this.onProfileUpdated,
  }) : super(key: key);

  @override
  _ProfileImageEditPageState createState() => _ProfileImageEditPageState();
}

class _ProfileImageEditPageState extends State<ProfileImageEditPage> {
  late Future<Map<String, dynamic>?> user;
  String newImage = '';

  @override
  void initState() {
    super.initState();
    newImage = widget.userInfo?['image_path'] ?? '';
  }

  Future<void> pickImage() async {
    final imagePicker = ImagePicker();
    final pickedImage =
        await imagePicker.pickImage(source: ImageSource.gallery);

    if (pickedImage != null) {
      final File imageFile = File(pickedImage.path);
      final String imagePath = imageFile.path;

      setState(() {
        newImage = imagePath;
      });
    }
  }

  Future<void> saveImageChanges() async {
    final profileApiService = ProfileApiService();
    final response = await profileApiService.putRequest('user', {
      'image_path': newImage,
    });

    if (response.statusCode == 200) {
      print('프로필 수정 성공');

      if (widget.onProfileUpdated != null) {
        final updatedUserInfo = widget.userInfo ?? {};
        updatedUserInfo['image_path'] = newImage;
        widget.onProfileUpdated!(updatedUserInfo);
      }
    } else {
      print('프로필 수정 실패 - 상태 코드: ${response.statusCode}');
      // 실패 시 에러 처리
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        backgroundColor: Colors.white,
        elevation: 2,
        title: const Text(
          '프로필 수정',
          style: TextStyle(
            fontSize: 21,
            fontWeight: FontWeight.w600,
            color: Colors.black,
          ),
        ),
        iconTheme: const IconThemeData(color: Colors.black),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const SizedBox(height: 20),
            GestureDetector(
              onTap: pickImage,
              child: CircleAvatar(
                radius: 50,
                backgroundImage:
                    newImage.isNotEmpty ? FileImage(File(newImage)) : null,
                child: newImage.isEmpty ? const Icon(Icons.add_a_photo) : null,
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                saveImageChanges();
                Navigator.of(context).pop({'image_path': newImage});
              },
              style: ElevatedButton.styleFrom(
                foregroundColor: Colors.black,
                backgroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(
                  vertical: 10,
                  horizontal: 20,
                ),
                side: const BorderSide(color: Colors.black),
              ),
              child: const Text(
                '완료',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
