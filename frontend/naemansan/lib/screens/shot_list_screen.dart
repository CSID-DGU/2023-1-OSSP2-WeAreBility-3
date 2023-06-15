import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:naemansan/services/login_api_service.dart';

class ShotList extends StatefulWidget {
  final double? latitude;
  final double? longitude;

  const ShotList({
    Key? key,
    this.latitude,
    this.longitude,
  }) : super(key: key);

  @override
  State<ShotList> createState() => _ShotListState();
}

class _ShotListState extends State<ShotList> {
  double? _latitude;
  double? _longitude;
  List<dynamic> dataList = [];

  @override
  initState() {
    super.initState();
    _getCurrentLocation();
  }

  _getCurrentLocation() async {
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
    try {
      final position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );
      setState(() {
        _latitude = position.latitude;
        _longitude = position.longitude;
      });
      fetchItems();
    } catch (error) {
      // print('Error fetching location: $error');
    }
  }

  Future<void> fetchItems() async {
    ApiService apiService = ApiService();
    List<dynamic>? data;

    data = await apiService.getLocationBasedShapList(_latitude, _longitude);
    setState(() {
      dataList = data ?? [];
    });
    // print(data);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('상권 리스트'),
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
      ),
      body: dataList.isEmpty
          ? const Center(
              child: Text('등록된 상권 리스트가 없습니다.'),
            )
          : ListView.builder(
              itemCount: dataList.length,
              itemBuilder: (context, index) {
                final item = dataList[index];
                final name = item['name'] ?? '';
                final introduction = item['introduction'] ?? '';

                return ListTile(
                  title: Text(name,
                      style: const TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      )),
                  subtitle: Text(introduction,
                      style: const TextStyle(
                        fontSize: 14,
                      )),

                  // Add other details if needed
                );
              },
            ),
    );
  }
}
