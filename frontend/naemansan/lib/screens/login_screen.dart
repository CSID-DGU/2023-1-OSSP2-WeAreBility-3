import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:naemansan/widgets/login_button.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  dynamic userInfo = ''; // storage에 있는 유저 정보를 저장
  final GoogleSignIn googleSignIn = GoogleSignIn();
  static const storage =
      FlutterSecureStorage(); // FlutterSecureStorage를 storage로 저장
  bool isLoading = true; // 로딩 상태를 나타내는 변수

  @override
  void initState() {
    init();
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _asyncMethod();
    });
  }

  _asyncMethod() async {
    setState(() {
      isLoading = true; // 로딩 상태 갱신
    });

    userInfo = await storage.read(key: "login");

    if (userInfo != null) {
      goIndex();
    } else {}
    setState(() {
      isLoading = false; // 로딩 상태 갱신
    });
  }

  init() async {
    String deviceToken = await getDeviceToken();
    print("###### PRINT DEVICE TOKEN TO USE FOR PUSH NOTIFCIATION ######");
    print(deviceToken);
    print("############################################################");

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

  goIndex() {
    Navigator.pushNamedAndRemoveUntil(context, '/index', (route) => false);
  }

  Widget _buildLoginButtons() {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const SizedBox(height: 60),
        LoginBtn(
          whatsLogin: "Kakao 계정으로 로그인",
          logo: "kakao",
          routeContext: context,
        ),
        const SizedBox(height: 20),
        LoginBtn(
          whatsLogin: "Apple 계정으로 로그인",
          logo: "apple",
          routeContext: context,
        ),
        const SizedBox(height: 20),
        LoginBtn(
          whatsLogin: "Google 계정으로 로그인",
          logo: "google",
          routeContext: context,
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
          fit: BoxFit.cover,
          image: AssetImage('assets/images/login_screen.png'),
        ),
      ),
      child: Scaffold(
        backgroundColor: Colors.transparent,
        body: Center(
          child: isLoading
              ? const CircularProgressIndicator(
                  color: Colors.black,
                ) // 로딩 중에 표시될 위젯
              : _buildLoginButtons(), // 로딩이 완료된 후에 표시될 위젯
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
    print(deviceToken);
    return (deviceToken == null) ? "" : deviceToken;
  }
}
