import 'dart:async';

import 'package:flutter/material.dart';
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
  bool _isTracking = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: const Text('산책로 추가'),
      ),
      body: NaverMap(
        onMapCreated: onMapCreated,
        mapType: _mapType,
        initLocationTrackingMode: _isTracking
            ? LocationTrackingMode.Follow
            : LocationTrackingMode.None,
        locationButtonEnable: true,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _isTracking ? stopWalking : startWalking,
        child: Text(_isTracking ? "산책 종료" : "산책 시작"),
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
    });
  }

  void stopWalking() async {
    setState(() {
      _isTracking = false;
    });

    final NaverMapController controller = await _controller.future;

    print("Current Location: $_currentLocation");
  }
}
