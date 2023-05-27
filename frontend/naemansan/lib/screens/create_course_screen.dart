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
  final List<Map<String, double>> _locations = [];
  bool _isWalking = false;
  bool _isTitleInputEnabled = false;
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
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              controller: _titleController,
              decoration: const InputDecoration(
                labelText: '산책로 제목',
                enabledBorder: OutlineInputBorder(
                  borderSide: BorderSide(color: Colors.black87),
                ),
                focusedBorder: OutlineInputBorder(
                  borderSide: BorderSide(color: Colors.black87),
                ),
                labelStyle: TextStyle(color: Colors.black87),
              ),
              enabled: _isTitleInputEnabled,
            ),
            const SizedBox(height: 16),
            const Text(
              '제목 입력 후 산책 시작 버튼을 눌러주세요.',
              style: TextStyle(fontSize: 16),
            ),
            Text(
              '위도: ${_latitude ?? 'N/A'}',
              style: const TextStyle(fontSize: 16),
            ),
            Text(
              '경도: ${_longitude ?? 'N/A'}',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed:
                  _isWalking || !_isTitleInputEnabled ? null : _startWalk,
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.transparent,
                elevation: 0,
                side: const BorderSide(color: Colors.black87),
              ),
              child:
                  const Text('산책 시작', style: TextStyle(color: Colors.black87)),
            ),
            ElevatedButton(
              onPressed: _isWalking ? _endWalk : _completeTitleInput,
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.transparent,
                elevation: 0,
                side: const BorderSide(color: Colors.black87),
              ),
              child: _isWalking
                  ? const Text('산책 종료', style: TextStyle(color: Colors.black87))
                  : const Text('제목 입력 하기',
                      style: TextStyle(color: Colors.black87)),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: _locations.length,
                itemBuilder: (context, index) {
                  final location = _locations[index];
                  final latitude = location['latitude'] ?? 0.0;
                  final longitude = location['longitude'] ?? 0.0;
                  return ListTile(
                    title: Text('위도: $latitude, 경도: $longitude'),
                  );
                },
              ),
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
    if (permissionStatus == LocationPermission.denied) {
      final permissionRequested = await Geolocator.requestPermission();
      if (permissionRequested != LocationPermission.whileInUse &&
          permissionRequested != LocationPermission.always) {
        setState(() {
          _isWalking = false;
          _isTitleInputEnabled = true;
        });
        return;
      }
    }

    final positionStream = Geolocator.getPositionStream();

    positionStream.listen((Position position) {
      setState(() {
        _latitude = position.latitude;
        _longitude = position.longitude;
        final location = {
          'latitude': position.latitude,
          'longitude': position.longitude,
        };
        _locations.add(location);
      });
    });
  }

  void _endWalk() async {
    if (!_isWalking) return;

    // 경고 다이얼로그 표시
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('산책 종료'),
          content: const Text('산책이 종료됐습니다!'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context); // 다이얼로그 닫기
              },
              child: const Text('확인'),
            ),
          ],
        );
      },
    );

    setState(() {
      _isWalking = false;
    });

    ApiService apiService = ApiService();

    final String title = _titleController.text;

    final Map<String, dynamic> courseData = {
      'title': title,
      'locations': _locations,
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
      _isTitleInputEnabled = true;
      _isTitleEntered = true;
    });
  }
}
