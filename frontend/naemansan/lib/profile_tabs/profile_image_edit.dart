import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:naemansan/services/mypage_api_service.dart';

class ProfileImageEditPage extends StatefulWidget {
  final Map<String, dynamic>? userInfo;

  const ProfileImageEditPage({
    Key? key,
    required this.userInfo,
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
    pickImage();
    if (_image == null) {
      print('이미지를 선택하세요.');
      return;
    }
    //선택한 이미지 저장
    await ProfileApiService().updateProfilePicture(_image!);
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
                saveImageChanges(); //완료 버튼 클릭 시 이미지 저장
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
