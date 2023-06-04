import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:naemansan/services/mypage_api_service.dart';

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
  File? _image;

  Future<void> pickImage() async {
    final imagePicker = ImagePicker();
    final pickedImage =
        await imagePicker.pickImage(source: ImageSource.gallery);

    if (pickedImage != null) {
      setState(() {
        _image = File(pickedImage.path);
      });
    }
  }

  Future<void> saveImageChanges() async {
    if (_image == null) {
      print('이미지를 선택하세요.');
      return;
    }

    final profileApiService = ProfileApiService();

    // postRequest 호출
    final response = await profileApiService
        .postRequest('image/user', {'image': _image!.path});

    if (response.statusCode == 200) {
      print('프로필 수정 성공');
      // 프로필 수정 완료 후 다른 작업 수행

      // 변경된 프로필 정보를 이전 페이지로 전달
      if (widget.onProfileUpdated != null) {
        final updatedUserInfo = widget.userInfo ?? {};
        updatedUserInfo['image'] = _image!.path;
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
                backgroundImage: _image != null ? FileImage(_image!) : null,
                child: _image == null ? const Icon(Icons.add_a_photo) : null,
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                saveImageChanges();
                Navigator.of(context).pop(true);
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
                  fontSize: 16,
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
