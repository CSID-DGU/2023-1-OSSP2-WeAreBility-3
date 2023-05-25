import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:naemansan/services/login_api_service.dart';

class CreateCourseScreen extends StatefulWidget {
  const CreateCourseScreen({Key? key}) : super(key: key);

  @override
  _CreateCourseScreenState createState() => _CreateCourseScreenState();
}

class _CreateCourseScreenState extends State<CreateCourseScreen> {
  final TextEditingController _titleController = TextEditingController();
  double? _latitude;
  double? _longitude;
  bool _isWalking = false;
  bool _isTitleInputEnabled = true;
  bool _isTitleEntered = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: const Text('산책로 추가'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _titleController,
              decoration: const InputDecoration(labelText: '산책로 제목'),
              enabled: _isTitleInputEnabled,
            ),
            Text(
              '위도: ${_latitude ?? 'N/A'}',
              style: const TextStyle(fontSize: 16),
            ),
            Text(
              '경도: ${_longitude ?? 'N/A'}',
              style: const TextStyle(fontSize: 16),
            ),
            ElevatedButton(
              onPressed:
                  _isWalking || !_isTitleInputEnabled ? null : _startWalk,
              child: const Text('산책 시작'),
            ),
            ElevatedButton(
              onPressed: _isWalking ? _endWalk : _completeTitleInput,
              child: _isWalking ? const Text('산책 종료') : const Text('제목 입력 완료'),
            ),
          ],
        ),
      ),
    );
  }

  void _startWalk() async {
    setState(() {
      _isWalking = true;
      _isTitleInputEnabled = false;
    });

    final permissionStatus = await Geolocator.checkPermission();
    print(permissionStatus);
    if (permissionStatus == LocationPermission.denied) {
      final permissionRequested = await Geolocator.requestPermission();
      if (permissionRequested != LocationPermission.whileInUse &&
          permissionRequested != LocationPermission.always) {
        print("sadasdsad");
        setState(() {
          _isWalking = false;
          _isTitleInputEnabled = true;
        });
        return;
      }
    }

    final positionStream = Geolocator.getPositionStream();
    print(positionStream.listen((event) {}));

    positionStream.listen((Position position) {
      setState(() {
        _latitude = position.latitude;
        _longitude = position.longitude;
      });
    });
  }

  void _endWalk() async {
    if (!_isWalking) return;

    setState(() {
      _isWalking = false;
    });

    ApiService apiService = ApiService();

    final String title = _titleController.text;
    final List<Map<String, double>> locations = [
      {
        'latitude': _latitude ?? 0.0,
        'longitude': _longitude ?? 0.0,
      },
    ];

    final Map<String, dynamic> courseData = {
      'title': title,
      'locations': locations,
    };
    print(courseData);

    final Map<String, dynamic> response =
        await apiService.registerIndividualCourse(courseData);
    if (response['success'] == true) {
      // Course registration successful, handle navigation or display a success message
      print("산책로 등록 성공ㅋ");
      final responseData = response['data'];
      final int id = responseData['id'];
      final double distance = responseData['distance'];
      print("ID: $id");
      print("Distance: $distance");
    } else {
      // Course registration failed, handle the error or display an error message
      print("산책로 실패 ㅅㅂ");
      final error = response['error'];
      print("Error: $error");
    }
  }

  void _completeTitleInput() {
    setState(() {
      _isTitleInputEnabled = false;
      _isTitleEntered = true;
    });
  }
}
