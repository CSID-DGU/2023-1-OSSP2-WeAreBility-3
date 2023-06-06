import 'dart:async';

import 'package:flutter/material.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class DetailMap extends StatefulWidget {
  final List<Map<String, dynamic>> locations;

  const DetailMap({super.key, required this.locations});

  @override
  State<DetailMap> createState() => _DetailMapState();
}

class _DetailMapState extends State<DetailMap> {
  Completer<NaverMapController> _controller = Completer();

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
    });
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      // 크기
      height: 300,
      child: NaverMap(
        onMapCreated: onMapCreated,
        mapType: _mapType,
        initLocationTrackingMode: LocationTrackingMode.None,
        initialCameraPosition: CameraPosition(
          target: LatLng(widget.locations[0]['latitude'] as double,
              widget.locations[1]['longitude'] as double),
          zoom: 15,
        ),
        locationButtonEnable: false,
        pathOverlays: _pathOverlays,
      ),
    );
  }
}
