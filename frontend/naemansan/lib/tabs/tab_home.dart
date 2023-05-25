//홈 페이지 Home()

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:naemansan/screens/notification_screen.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/widgets/banner.dart';
import 'package:naemansan/widgets/horizontal_slider.dart';
import 'package:naemansan/widgets/main_slider.dart';
import 'dart:convert';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  late Future<Map<String, dynamic>?> user;
  String _city = "";
  String _district = "";
  String _street = "";
  bool nowLocation = false;

  String keyword = "한강"; // 기본 키워드 값

// Set the latitude and longitude values
  late double _latitude = 0.0;
  late double _longitude = 0.0;
  @override
  void initState() {
    super.initState();
    ApiService apiService = ApiService();
    user = apiService.getUserInfo();
  }

  // 위도, 경도로 주소 가져오기
  // 주소 가져오기 (위도, 경도 -> 주소)
  // 주소 가져오기 (위도, 경도 -> 주소)
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
    setState(() {
      nowLocation = false; // Reset the nowLocation flag
    });
    try {
      final position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );
      _latitude = position.latitude;
      _longitude = position.longitude;
      await _getAddressFromLatLng(_latitude, _longitude);
      setState(() {
        nowLocation =
            true; // Update the nowLocation flag after successfully retrieving the address
      });
    } catch (error) {
      setState(() {
        nowLocation = false; // Update the nowLocation flag if an error occurs
      });
      print('Error fetching location: $error');
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
        if (types.contains("sublocality_level_4")) {
          // print(_street);
          _street = results[i]["long_name"];
        }
        // print(results[i]);
      }
      setState(() {
        _latitude = latitude;
        _longitude = longitude;
      });
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
            const Expanded(child: SizedBox(width: 30)), // 여백 추가
            IconButton(
              padding: const EdgeInsets.only(left: 25),
              icon: const Icon(
                Icons.notifications_none_rounded,
                color: Colors.black,
              ),
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
                          ? Text("현재 위치:$_district, $_city $_street ")
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
                  AnimatedSwitcher(
                    duration: const Duration(milliseconds: 500),
                    transitionBuilder:
                        (Widget child, Animation<double> animation) {
                      return FadeTransition(
                        opacity: animation,
                        child: child,
                      );
                    },
                    child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: nowLocation
                            ? [
                                MainSlider(
                                  title: "🌿 위치별",
                                  sliderWidget: HorizontalSlider(
                                    latitude: _latitude,
                                    longitude: _longitude,
                                  ),
                                ),
                                const MainSlider(
                                  title: "🎋 키워드별",
                                  sliderWidget: HorizontalSlider(
                                    keyword: "한강",
                                  ),
                                ),
                                const MainSlider(
                                  title: "🍽️ 상권",
                                  sliderWidget: HorizontalSlider(),
                                ),
                              ]
                            : [
                                const SizedBox(height: 30),
                                const Text('🌿 위치별',
                                    style: TextStyle(
                                        fontSize: 25,
                                        fontWeight: FontWeight.w700,
                                        color: Colors.black87)),
                                const SizedBox(height: 20),
                                const Text('현재 위치를 동기화시켜 주세요!',
                                    style: TextStyle(
                                        fontSize: 18,
                                        fontWeight: FontWeight.w600,
                                        color: Colors.black87)),
                                const SizedBox(height: 50),
                                const MainSlider(
                                  title: "🎋 키워드별",
                                  sliderWidget: HorizontalSlider(
                                    keyword: "한강",
                                  ),
                                ),
                                const MainSlider(
                                  title: "🍽️ 상권",
                                  sliderWidget: HorizontalSlider(),
                                ),
                              ]),
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
