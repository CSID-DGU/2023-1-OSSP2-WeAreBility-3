import 'dart:async';

import 'package:flutter/material.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class UsingCourseScreen extends StatefulWidget {
  final List<Map<String, dynamic>> locations;

  const UsingCourseScreen({
    super.key,
    required this.locations,
  });

  @override
  State<UsingCourseScreen> createState() => _UsingCourseScreenState();
}

class _UsingCourseScreenState extends State<UsingCourseScreen> {
  Completer<NaverMapController> _controller = Completer();
  late double startLat;
  late double startLng;
  late double lastLat;
  late double lastLng;
  late double cameraLat;
  late double cameraLng;

  @override
  void initState() {
    print(widget.locations);
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
      print(coordinates);
      startLat = coordinates.first.latitude;
      startLng = coordinates.first.longitude;
      lastLat = coordinates.last.latitude;
      lastLng = coordinates.last.longitude;
      cameraLat = (startLat + lastLat) / 2;
      cameraLng = (startLng + lastLng) / 2;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Column(
      children: [
        Expanded(
          child: NaverMap(
            onMapCreated: onMapCreated,
            mapType: _mapType,
            minZoom: 7, //지도의 최소 줌 레벨
            zoomGestureEnable: true,
            scrollGestureEnable: true,
            initLocationTrackingMode: LocationTrackingMode.None,

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
            // 카메라 이동 가능

            initialCameraPosition: CameraPosition(
              // target: LatLng(widget.locations[0]['latitude'] as double,
              //     widget.locations[1]['longitude'] as double),
              target: LatLng(cameraLat, cameraLng),
              zoom: 13,
            ),
            locationButtonEnable: false,
            pathOverlays: _pathOverlays,
          ),
        ),
      ],
    ));
  }
}
