//홈 페이지 Home()

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:naemansan/screens/notification_screen.dart';
import 'package:naemansan/widgets/banner.dart';
import 'package:naemansan/widgets/horizontal_slider.dart';
import 'package:naemansan/widgets/notification.dart';
import 'dart:convert';
import 'package:permission_handler/permission_handler.dart';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  String _city = "";
  String _district = "";
  bool nowLocation = false;

  @override
  void initState() {
    super.initState();
  }

  // 위도, 경도로 주소 가져오기
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
    final position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high);
    _getAddressFromLatLng(position.latitude, position.longitude);
  }

  Future<void> requestLocationPermission() async {
    final PermissionStatus permissionStatus =
        await Permission.locationWhenInUse.request();

    // 위치 권한 요청
    if (permissionStatus == PermissionStatus.granted) {
      // 권한 허용 시 처리할 코드
    } else {
      // 권한 거부 시 처리할 코드
    }
  }

// 주소 가져오기 (위도, 경도 -> 주소)
// 주소 가져오기 (위도, 경도 -> 주소)
  _getAddressFromLatLng(latitude, longitude) async {
    // 환경변수 로드
    await dotenv.load(fileName: 'assets/config/.env');
    // url 생성
    final url =
        "https://maps.googleapis.com/maps/api/geocode/json?latlng=$latitude,$longitude&key=${dotenv.env['GOOGLE_MAPS_API_KEY']}&language=ko";
    final response = await http.get(Uri.parse(url));
    final responseData = json.decode(response.body);
    if (responseData["status"] == "OK") {
      final results = responseData["results"][0]["address_components"];
      for (var i = 0; i < results.length; i++) {
        final types = results[i]["types"];
        // "locality"나 "administrative_area_level_1" 이외에도 "sublocality", "neighborhood" 등 다른 값도 가능
        if (types.contains("locality") || types.contains("sublocality")) {
          _city = results[i]["long_name"];
        }
        if (types.contains("administrative_area_level_1")) {
          _district = results[i]["long_name"];
        }
      }
      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        automaticallyImplyLeading: false, // 앱바의 뒤로가기 버튼을 없애기 위해 false로 설정

        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: [
                  const Text(
                    '내만산',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const SizedBox(width: 5),
                  Image.asset(
                    'assets/images/logo.png',
                    width: 18,
                  ),
                ],
              ),
            ),
            const Spacer(),
            // 알림 icon
            NotificationIcon(
              hasUnreadNotifications: true, // 읽지 않은 알림이 있는 경우 true로 설정
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => const NotificationScreen()),
                );
              },
            ),
          ],
        ),
      ),
      // body
      body: SingleChildScrollView(
        child: Column(
          children: [
            BannerSwiper(),
            Padding(
              padding: const EdgeInsets.only(left: 25, top: 10, bottom: 20),
              child: Column(
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      const Icon(Icons.location_on_rounded, size: 20),
                      const SizedBox(width: 5),
                      nowLocation
                          ? Text("현재 위치:$_city $_district")
                          : const Text("위치 정보 없음"),
                      IconButton(
                        onPressed: () {
                          _getCurrentLocation();
                          setState(() {
                            nowLocation = true;
                          });
                        },
                        icon: const Icon(Icons.refresh_rounded),
                      ),
                    ],
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: const [
                      Text(
                        "🌿 위치별",
                        style: TextStyle(
                          fontSize: 25,
                          fontWeight: FontWeight.w800,
                        ),
                      ),
                      SizedBox(
                        height: 20,
                      ),
                      SizedBox(
                        height: 170,
                        child: HorizontalSlider(),
                      ),
                      SizedBox(
                        height: 20,
                      ),
                      Text(
                        "🎋 카테고리별",
                        style: TextStyle(
                          fontSize: 25,
                          fontWeight: FontWeight.w800,
                        ),
                      ),
                      SizedBox(
                        height: 20,
                      ),
                      SizedBox(
                        height: 170,
                        child: HorizontalSlider(),
                      ),
                      SizedBox(
                        height: 20,
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
