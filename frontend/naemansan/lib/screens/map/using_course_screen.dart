import 'dart:async';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'package:geolocator/geolocator.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class UsingCourseScreen extends StatefulWidget {
  final List<Map<String, dynamic>> locations;

  const UsingCourseScreen({
    Key? key,
    required this.locations,
  }) : super(key: key);

  @override
  _UsingCourseScreenState createState() => _UsingCourseScreenState();
}

class _UsingCourseScreenState extends State<UsingCourseScreen> {
  Completer<NaverMapController> _controller = Completer();
  late double startLat;
  late double startLng;
  late double lastLat;
  late double lastLng;
  late double cameraLat;
  late double cameraLng;
  bool isCourseStarted = false;
  DateTime? courseStartTime;
  DateTime? courseEndTime;

  @override
  void initState() {
    _drawPath();
    super.initState();
  }

  final MapType _mapType = MapType.Basic;
  LatLng? _currentLocation;

  final Set<PathOverlay> _pathOverlays = {};

  void onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted) _controller = Completer();
    _controller.complete(controller);
  }

  void _drawPath() {
    final List<LatLng> coordinates = [];

    for (final location in widget.locations) {
      final latitude = location['latitude'] as double;
      final longitude = location['longitude'] as double;
      final latLng = LatLng(latitude, longitude);
      coordinates.add(latLng);
    }

    final pathOverlay = PathOverlay(
      PathOverlayId('walking_path'),
      coordinates,
      width: 10,
      color: Colors.green,
      outlineColor: Colors.transparent,
    );

    setState(() {
      _pathOverlays.add(pathOverlay);
      startLat = coordinates.first.latitude;
      startLng = coordinates.first.longitude;
      lastLat = coordinates.last.latitude;
      lastLng = coordinates.last.longitude;
      cameraLat = (startLat + lastLat) / 2;
      cameraLng = (startLng + lastLng) / 2;
    });
  }

  void startCourse() {
    setState(() {
      isCourseStarted = true;
      courseStartTime = DateTime.now();
    });
  }

  void endCourse() {
    setState(() {
      isCourseStarted = false;
      courseEndTime = DateTime.now();
    });
  }

  String getCourseDuration() {
    if (isCourseStarted) {
      final endTime = courseEndTime ?? DateTime.now();
      final duration = endTime.difference(courseStartTime ?? endTime);
      final formattedDuration =
          duration.toString().split('.').first.padLeft(8, '0');
      return formattedDuration;
    } else {
      return '00:00:00';
    }
  }

  void cantWalking() {
    Fluttertoast.showToast(
      msg: "출발/도착 지점에 가셔야 버튼이 활성화 됩니다.",
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM,
      backgroundColor: Colors.red,
      textColor: Colors.white,
    );
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
      // _startTimer();

      setState(() {
        _updateCurrentLocation(position.latitude, position.longitude);
      });
    } catch (e) {
      // print('위치 정보를 가져오는 중에 오류가 발생했습니다: $e');
    }
  }

  void _updateCurrentLocation(double latitude, double longitude) {
    _currentLocation = LatLng(latitude, longitude);
  }

  bool isInsideRoute(LatLng currentLocation) {
    final double currentLat = currentLocation.latitude;
    final double currentLng = currentLocation.longitude;

    if (currentLat >= startLat &&
        currentLat <= lastLat &&
        currentLng >= startLng &&
        currentLng <= lastLng) {
      return true;
    }

    return false;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Expanded(
            child: NaverMap(
              circles: [
                CircleOverlay(
                  overlayId: 'start',
                  center: LatLng(startLat, startLng),
                  radius: 50,
                  color: Colors.green.withOpacity(0.3),
                  outlineColor: Colors.green,
                  outlineWidth: 2,
                ),
                CircleOverlay(
                  overlayId: 'end',
                  center: LatLng(lastLat, lastLng),
                  radius: 50,
                  color: Colors.red.withOpacity(0.3),
                  outlineColor: Colors.red,
                  outlineWidth: 2,
                ),
              ],
              onMapCreated: onMapCreated,
              mapType: _mapType,
              minZoom: 7,
              zoomGestureEnable: true,
              scrollGestureEnable: true,
              initLocationTrackingMode: LocationTrackingMode.Face,
              markers: [
                Marker(
                  markerId: "0",
                  position: LatLng(startLat, startLng),
                  captionText: "출발",
                  captionTextSize: 13,
                  iconTintColor: const Color.fromARGB(255, 78, 221, 0),
                ),
                Marker(
                  markerId: "1",
                  position: LatLng(lastLat, lastLng),
                  captionText: "종료",
                  captionTextSize: 13,
                  iconTintColor: const Color.fromARGB(255, 255, 0, 0),
                ),
              ],
              initialCameraPosition: CameraPosition(
                target: LatLng(cameraLat, cameraLng),
                zoom: 14,
              ),
              locationButtonEnable: true,
              pathOverlays: _pathOverlays,
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: isInsideRoute(_currentLocation ?? const LatLng(0, 0))
            ? (isCourseStarted ? endCourse : startCourse)
            : cantWalking,
        label: Text(
          isCourseStarted ? '산책 종료하기' : '산책 시작하기',
          style: const TextStyle(
            color: Colors.white,
            fontSize: 16,
            fontWeight: FontWeight.w600,
          ),
        ),
        backgroundColor: isCourseStarted ? Colors.red : Colors.green.shade400,
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
      bottomNavigationBar: Container(
        padding: const EdgeInsets.all(16.0),
        color: Colors.grey[200],
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.timer),
            const SizedBox(width: 8.0),
            Text(
              '산책 시간: ${getCourseDuration()}',
              style: const TextStyle(
                fontSize: 16,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
