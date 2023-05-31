import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:naemansan/services/login_api_service.dart';

class CreateCourseScreen extends StatefulWidget {
  const CreateCourseScreen({Key? key}) : super(key: key);

  @override
  _CreateCourseScreenState createState() => _CreateCourseScreenState();
}

class _CreateCourseScreenState extends State<CreateCourseScreen> {
  final TextEditingController _titleController = TextEditingController();
  final List<LatLng> _locations = [];
  final Set<Marker> _markers = {}; // Added markers set
  bool _isWalking = false;
  bool _isTitleInputEnabled = true;
  bool _isTitleEntered = false;
  GoogleMapController? _mapController;
  Position? _currentPosition;

  @override
  void initState() {
    super.initState();
    _getCurrentLocation();
  }

  void _getCurrentLocation() async {
    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return Future.error('위치 권한이 없습니다.');
      }
    }
    if (permission == LocationPermission.deniedForever) {
      return Future.error('위치 권한이 영구적으로 없습니다.');
    }

    final GeolocatorPlatform geolocator = GeolocatorPlatform.instance;

    try {
      final position = await geolocator.getCurrentPosition();
      setState(() {
        _currentPosition = position;
      });
    } catch (e) {
      print('Failed to get current location: $e');
      // 위치 가져오기 실패 시 에러 처리 작업 추가
      setState(() {
        _currentPosition = null;
      });
    }
  }

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
              onChanged: (text) {
                setState(() {
                  // 입력된 텍스트가 있는지 확인하여 버튼 상태를 업데이트
                  _isTitleEntered = text.isNotEmpty;
                });
              },
            ),
            const SizedBox(height: 16),
            const Text(
              '제목 입력 후 산책 시작 버튼을 눌러주세요.',
              style: TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: GoogleMap(
                onMapCreated: _onMapCreated,
                initialCameraPosition: _currentPosition != null
                    ? CameraPosition(
                        target: LatLng(
                          _currentPosition!.latitude,
                          _currentPosition!.longitude,
                        ),
                        zoom: 15,
                      )
                    : const CameraPosition(
                        target: LatLng(0, 0),
                        zoom: 15,
                      ),
                polylines: {
                  Polyline(
                    polylineId: const PolylineId('courseRoute'),
                    color: Colors.blue,
                    points: _locations,
                  ),
                },
                markers: _markers, // Added markers set
              ),
            ),
            ElevatedButton(
              onPressed: _isWalking
                  ? _endWalk
                  : (_isTitleInputEnabled && _isTitleEntered)
                      ? _startWalk
                      : null,
              // _isWalking || !_isTitleInputEnabled ? _endWalk : _startWalk,
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.transparent,
                elevation: 0,
                side: const BorderSide(color: Colors.black87),
              ),
              child: _isWalking || !_isTitleInputEnabled
                  ? const Text('산책 종료',
                      style: TextStyle(
                        color: Colors.black87,
                      ))
                  : const Text(
                      '산책 시작',
                      style: TextStyle(color: Colors.black87),
                    ),
            ),
          ],
        ),
      ),
    );
  }

  void _onMapCreated(GoogleMapController controller) {
    _mapController = controller;
    if (_currentPosition != null) {
      final latitude = _currentPosition!.latitude;
      final longitude = _currentPosition!.longitude;
      final location = LatLng(latitude, longitude);
      _locations.add(location);
      _addMarker(latitude, longitude);
      _mapController!.animateCamera(
        CameraUpdate.newLatLngZoom(location, 15),
      );
    }
  }

  void _addMarker(double latitude, double longitude) {
    const markerId = MarkerId('currentLocation');
    final marker = Marker(
      markerId: markerId,
      position: LatLng(latitude, longitude),
      icon: BitmapDescriptor.defaultMarker,
    );
    setState(() {
      _markers.add(marker);
    });
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
        final latitude = position.latitude;
        final longitude = position.longitude;
        final location = LatLng(latitude, longitude);
        _locations.add(location);
        _addMarker(latitude, longitude); // Add marker for each position

        if (_mapController != null) {
          _mapController!.animateCamera(
            CameraUpdate.newLatLng(location),
          );
        }
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
                Navigator.pop(context); // 이전 페이지로 이동
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

    // Send the route data to the server and save it
    // …
  }

  void _completeTitleInput() {
    setState(() {
      _isTitleInputEnabled = true;
      _isTitleEntered = true;
    });
  }
}
