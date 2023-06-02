import 'dart:async';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class NaverMapScreen extends StatefulWidget {
  const NaverMapScreen({Key? key}) : super(key: key);

  @override
  _NaverMapScreenState createState() => _NaverMapScreenState();
}

class _NaverMapScreenState extends State<NaverMapScreen> {
  Completer<NaverMapController> _controller = Completer();

  final MapType _mapType = MapType.Basic;
  LatLng? _currentLocation;

  Timer? _timer; // 추가

  bool _isTracking = false;
  DateTime? _startTime;
  DateTime? _endTime;

  @override
  void initState() {
    super.initState();
    _getCurrentLocation();
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
      body: Column(
        children: [
          Expanded(
            child: NaverMap(
              onMapCreated: onMapCreated,
              mapType: _mapType,
              initLocationTrackingMode: _isTracking
                  ? LocationTrackingMode.Face
                  : LocationTrackingMode.None,
              locationButtonEnable: true,
              initialCameraPosition: _currentLocation != null
                  ? CameraPosition(
                      target: _currentLocation!,
                      zoom: 16,
                    )
                  : null,
            ),
          ),
          Container(
            // only top margin
            padding: const EdgeInsets.all(16),
            child: Text(
              _getDurationString(),
              style: const TextStyle(fontSize: 18),
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        onPressed: _isTracking ? stopWalking : startWalking,
        icon: Icon(_isTracking ? Icons.stop : Icons.add),
        label: Text(
          _isTracking ? '산책 종료하기' : "산책 시작하기",
          style: const TextStyle(
            fontSize: 15,
            fontWeight: FontWeight.w500,
          ),
        ),
      ),
    );
  }

  void onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted) _controller = Completer();
    _controller.complete(controller);
  }

  void startWalking() {
    setState(() {
      _isTracking = true;
      _startTime = DateTime.now();
    });
  }

  void stopWalking() async {
    setState(() {
      _isTracking = false;
      _endTime = DateTime.now();
    });
    _stopTimer();

    final NaverMapController controller = await _controller.future;

    final duration = _endTime!.difference(_startTime!);
    final hours = duration.inHours;
    final minutes = duration.inMinutes.remainder(60);
    final seconds = duration.inSeconds.remainder(60);
    goBack(hours, minutes, seconds);
  }

  goBack(hours, minutes, seconds) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('산책 종료'),
        content: Text('산책 시간: $hours시간 $minutes분 $seconds초'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pop(context);
            },
            child: const Text('확인'),
          ),
        ],
      ),
    );
  }

  String _getDurationString() {
    if (_isTracking) {
      final currentTime = DateTime.now();
      final duration = currentTime.difference(_startTime!);
      final hours = duration.inHours;
      final minutes = duration.inMinutes.remainder(60);
      final seconds = duration.inSeconds.remainder(60);
      return '현재 산책 시간: $hours시간 $minutes분 $seconds초';
    } else {
      return '산책을 시작해보세요!';
    }
  }

  void _getCurrentLocation() async {
    try {
      LocationPermission permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        // 위치 권한이 거부된 경우 처리
        return;
      }

      Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );
      _startTimer(); // 추가

      double latitude = position.latitude;
      double longitude = position.longitude;

      setState(() {
        _currentLocation = LatLng(latitude, longitude);
      });

      print('현재 위치: ($latitude, $longitude)');
    } catch (e) {
      print('위치 정보를 가져오는 중에 오류가 발생했습니다: $e');
    }
  }

  void _startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (_) {
      if (mounted) {
        setState(() {});
      }
    });
  }

  void _stopTimer() {
    _timer?.cancel();
    _timer = null;
  }
}
