//홈 페이지 Home()
import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  String _city = "";
  String _district = "";

  @override
  void initState() {
    super.initState();
    _getCurrentLocation();
  }

  // 위도, 경도로 주소 가져오기
  _getCurrentLocation() async {
    final position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high);
    _getAddressFromLatLng(position.latitude, position.longitude);
  }

// 주소 가져오기 (위도, 경도 -> 주소)
  _getAddressFromLatLng(latitude, longitude) async {
    // 환경변수 로드
    await dotenv.load(fileName: 'assets/config/.env');
    // url 생성
    final url =
        "https://maps.googleapis.com/maps/api/geocode/json?latlng=$latitude,$longitude&key=${dotenv.env['YOUR_NATIVE_APP_KEY']}";
    final response = await http.get(Uri.parse(url));
    final responseData = json.decode(response.body);
    if (responseData["status"] == "OK") {
      final results = responseData["results"][0]["address_components"];
      for (var i = 0; i < results.length; i++) {
        final types = results[i]["types"];
        if (types.contains("locality")) {
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
            IconButton(
              icon: const Icon(
                Icons.notifications_none_rounded,
                color: Colors.black,
              ),
              onPressed: () {
                // 버튼을 눌렀을 때 실행될 코드 작성
              },
            ),
          ],
        ),
      ),
    );
  }
}
