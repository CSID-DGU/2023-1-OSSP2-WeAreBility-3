//홈 페이지 Home()

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:naemansan/screens/notification_screen.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/widgets/banner.dart';
import 'package:naemansan/widgets/horizontal_slider.dart';
import 'package:naemansan/widgets/main_slider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';
import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

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
  List<bool> keywordButtonStates = [
    true,
    false,
    false,
    false,
  ]; //
  List<dynamic> myTagList = ["한강"];
  String selectedKeyword = "한강"; // 선택한 키워드 초기값
// Set the latitude and longitude values
  late double _latitude = 0.0;
  late double _longitude = 0.0;

  // list of keywords
  List<String> titleList = ["🌿 위치별", "🎋 키워드별", "🍽️ 상권"];

  static const storage = FlutterSecureStorage();
  dynamic userInfo = '';

  Future<void> deleteTokens() async {
    await storage.delete(key: 'accessToken');
    await storage.delete(key: 'refreshToken');
    final prefs = await SharedPreferences.getInstance();
    prefs.setBool('isLogged', false);
  }

  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      isTag();
    });
    init();

    super.initState();
    ApiService apiService = ApiService();
    user = apiService.getUserInfo();

    // user의 값이 null일때 loginScreen으로 이동하고 토큰값 지우기
    // print(user);

    user.then((value) {
      if (value == null) {
        goLogin();
      }
    });
    _getCurrentLocation();
    setState(() {
      nowLocation = true;
    });
  }

  Future<void> isTag() async {
    ApiService apiService = ApiService();
    var data = await apiService.getMyTag();

    if (data != null && data['success'] == true) {
      if (data['data'] != null && data['data']['tags'] != null) {
        // Tags exist
        if (data['data']['tags'].isEmpty) {
          // 테그 만들기 페이지로 이동
          // go to SelectTagScreen
          print(data['data']['tags']);
          if (mounted) {
            Navigator.pushNamedAndRemoveUntil(
                context, '/tagSelect', (route) => false);
          }
        } else {
          print('Tags exist');
        }
        List<dynamic> tags = data['data']['tags'];
        myTagList = tags.map((tag) => tag['name'] as String).toList();
        selectedKeyword = myTagList[0];
        print(myTagList);
      } else {
        // Tags do not exist
        print('No tags found');
      }
    } else {
      // Request failed or unsuccessful response
      print('Failed to retrieve data or unsuccessful response');
    }
  }

  goLogin() async {
    await deleteTokens();
    await storage.delete(key: 'login');
    gogoLogin();
  }

  gogoLogin() {
    Navigator.pushNamedAndRemoveUntil(context, '/login', (route) => false);
  }

  // push notification
  init() async {
    String deviceToken = await getDeviceToken();

    // print("###### PRINT DEVICE TOKEN TO USE FOR PUSH NOTIFCIATION ######");
    // print(deviceToken);
    // print("############################################################");

    // listen for user to click on notification
    FirebaseMessaging.onMessageOpenedApp.listen((RemoteMessage remoteMessage) {
      String? title = remoteMessage.notification!.title;
      String? description = remoteMessage.notification!.body;

      //im gonna have an alertdialog when clicking from push notification
      Alert(
        context: context,
        type: AlertType.error,
        title: title, // title from push notification data
        desc: description, // description from push notifcation data
        buttons: [
          DialogButton(
            onPressed: () => Navigator.pop(context),
            width: 120,
            child: const Text(
              "내가 만든 산책로, 내만산",
              style: TextStyle(color: Colors.white, fontSize: 20),
            ),
          )
        ],
      ).show();
    });
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
      if (mounted) {
        setState(() {
          nowLocation = false; // Update the nowLocation flag if an error occurs
        });
      }
      // print('Error fetching location: $error');
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
                      fontSize: 21,
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
                              const SizedBox(height: 10),
                              Text(
                                titleList[0],
                                style: const TextStyle(
                                  fontSize: 21,
                                  fontWeight: FontWeight.w800,
                                ),
                              ),
                              MainSlider(
                                title: titleList[0],
                                sliderWidget: HorizontalSlider(
                                  latitude: _latitude,
                                  longitude: _longitude,
                                  title: titleList[0],
                                ),
                              ),
                              Text(
                                titleList[1],
                                style: const TextStyle(
                                  fontSize: 21,
                                  fontWeight: FontWeight.w800,
                                ),
                              ),
                              const SizedBox(
                                height: 10,
                              ),
                              Row(
                                children: [
                                  _buildKeywordButton(myTagList[0], index: 0),
                                  if (myTagList.length > 1)
                                    _buildKeywordButton(myTagList[1], index: 1),
                                  if (myTagList.length > 2)
                                    _buildKeywordButton(myTagList[2], index: 2),
                                  _buildKeywordButton("변경하기", index: 3),

                                  // Add more keyword buttons as needed
                                ],
                              ),
                              MainSlider(
                                title: titleList[1],
                                sliderWidget: HorizontalSlider(
                                  keyword: selectedKeyword,
                                  title: titleList[1],
                                ),
                              ),
                              Text(
                                titleList[2],
                                style: const TextStyle(
                                  fontSize: 21,
                                  fontWeight: FontWeight.w800,
                                ),
                              ),
                              MainSlider(
                                title: titleList[2],
                                sliderWidget: HorizontalSlider(
                                  title: titleList[2],
                                ),
                              ),
                            ]
                          : [
                              const SizedBox(height: 10),
                              Text(titleList[0],
                                  style: const TextStyle(
                                      fontSize: 21,
                                      fontWeight: FontWeight.w700,
                                      color: Colors.black87)),
                              const SizedBox(height: 20),
                              const Text('현재 위치를 동기화시켜 주세요!',
                                  style: TextStyle(
                                      fontSize: 15,
                                      fontWeight: FontWeight.w600,
                                      color: Colors.black87)),
                              const SizedBox(height: 50),
                              Text(
                                titleList[1],
                                style: const TextStyle(
                                  fontSize: 21,
                                  fontWeight: FontWeight.w800,
                                ),
                              ),
                              MainSlider(
                                title: titleList[1],
                                sliderWidget: HorizontalSlider(
                                  keyword: selectedKeyword,
                                  title: titleList[1],
                                ),
                              ),
                              Text(
                                titleList[0],
                                style: const TextStyle(
                                  fontSize: 21,
                                  fontWeight: FontWeight.w800,
                                ),
                              ),
                              MainSlider(
                                title: titleList[2],
                                sliderWidget: HorizontalSlider(
                                  title: titleList[2],
                                ),
                              ),
                            ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _updateSelectedKeyword(String keyword) {
    setState(() {
      selectedKeyword = keyword;
      print("keyword: $selectedKeyword");
    });
  }

  Widget _buildKeywordButton(String keyword, {required int index}) {
    return Container(
      margin: const EdgeInsets.only(right: 10.0),
      child: ElevatedButton(
        onPressed: () {
          _updateSelectedKeyword(keyword);

          setState(() {
            // Update the button states based on the index
            for (int i = 0; i < keywordButtonStates.length; i++) {
              keywordButtonStates[i] = (i == index);
            }
            if (mounted && index == 3) {
              Navigator.pushNamedAndRemoveUntil(
                  context, '/tagSelect', (route) => false);
            }
          });
        },
        style: ButtonStyle(
          backgroundColor: MaterialStateProperty.all<Color>(
            keywordButtonStates[index]
                ? const Color.fromARGB(255, 26, 167, 85)
                : Colors.white,
          ),
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
            RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20),
            ),
          ),
        ),
        child: Text(
          keyword,
          style: TextStyle(
            color: keywordButtonStates[index] ? Colors.white : Colors.black,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  //get device token to use for push notification
  Future getDeviceToken() async {
    //request user permission for push notification

    FirebaseMessaging.instance.requestPermission();
    FirebaseMessaging firebaseMessage = FirebaseMessaging.instance;
    String? deviceToken = await firebaseMessage.getToken();
    bool isIos = true;
    if (mounted) {
      bool isIos = Theme.of(context).platform == TargetPlatform.iOS;
    }
    // deviceToekn, apple iOS여부 보내기
    // await apiService.sendDeviceToken(deviceToken, isIos);
    print("??");
    print("iOS,Android여부 : $isIos");
    return (deviceToken == null) ? "" : deviceToken;
  }
}
