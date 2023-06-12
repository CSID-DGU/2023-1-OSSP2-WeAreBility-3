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

  Timer? _timer;
  Timer? _timer2;
  bool _isTracking = false;
  DateTime? _startTime;
  DateTime? _endTime;

  final Set<PathOverlay> _pathOverlays = {};

  @override
  void initState() {
    super.initState();
  }

  void _getCurrentLocation() async {
    try {
      LocationPermission permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return;
      }

      Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );
      _startTimer();

      setState(() {
        _updateCurrentLocation(position.latitude, position.longitude);
      });
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

  void _updateCurrentLocation(double latitude, double longitude) {
    _currentLocation = LatLng(latitude, longitude);
    _drawPath();
  }

  void _drawPath() {
    if (_isTracking && _currentLocation != null) {
      final List<LatLng> coordinates =
          _pathOverlays.expand((overlay) => overlay.coords).toList();

      if (coordinates.isNotEmpty) {
        final lastCoordinate = coordinates.last;
        if (lastCoordinate.latitude == _currentLocation!.latitude &&
            lastCoordinate.longitude == _currentLocation!.longitude) {
          return;
        }
      } else {
        coordinates.add(_currentLocation!);
      }
      coordinates.add(_currentLocation!);
      print(coordinates);

      final pathOverlay = PathOverlay(
        PathOverlayId('walking_path'),
        coordinates,
        width: 10,
        color: Colors.green,
        outlineColor: Colors.transparent,
      );

      print('현재 pathOverlay: ${pathOverlay.coords}');

      setState(() {
        _pathOverlays.add(pathOverlay);
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
        title: Text(_getDurationString(),
            style: const TextStyle(
              fontSize: 21,
              fontWeight: FontWeight.w500,
            )),
      ),
      body: Column(
        children: [
          // Container(
          //   padding: const EdgeInsets.all(16),
          //   child: Text(
          //     _getDurationString(),
          //     style: const TextStyle(fontSize: 18),
          //   ),
          // ),
          Expanded(
            child: NaverMap(
              onMapCreated: onMapCreated,
              mapType: _mapType,
              initLocationTrackingMode: LocationTrackingMode.Face,
              locationButtonEnable: true,
              pathOverlays: _pathOverlays,
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
          _isTracking ? '산책 종료하기' : '산책 시작하기',
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

    _timer2 = Timer.periodic(const Duration(seconds: 1), (_) {
      if (mounted) {
        _getCurrentLocation();
      }
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

  void goBack(int hours, int minutes, int seconds) async {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('산책 종료'),
        content: Text('산책 시간: $hours시간 $minutes분 $seconds초'),
        actions: [
          TextButton(
            onPressed: () async {
              // 이동할 화면으로부터 데이터를 받기 위해 `pushNamed` 메소드를 사용합니다.

              await Navigator.pushNamed(
                context,
                '/createTitle',
                arguments: _pathOverlays
                    .map((pathOverlay) => {
                          'latitude': pathOverlay.coords[0].latitude,
                          'longitude': pathOverlay.coords[0].longitude,
                        })
                    .toList(),
              );
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
      return '산책 시간: $hours시간 $minutes분 $seconds초';
    } else {
      return '산책을 시작해보세요!';
    }
  }

  void _stopTimer() {
    _timer?.cancel();
    _timer2?.cancel();
    _timer = null;
    _timer2 = null;
  }
}
