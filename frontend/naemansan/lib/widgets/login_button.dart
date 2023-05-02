import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk_talk.dart';

class LoginBtn extends StatelessWidget {
  final String whatsLogin;
  final String logo;

  const LoginBtn({super.key, required this.whatsLogin, required this.logo});

  login() async {
    if (await isKakaoTalkInstalled()) {
      try {
        await UserApi.instance.loginWithKakaoTalk();
        // print('카카오톡으로 로그인 성공');
      } catch (error) {
        // print('카카오톡으로 로그인 실패 $error');

        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
        if (error is PlatformException && error.code == 'CANCELED') {
          return;
        }
        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인
        try {
          await UserApi.instance.loginWithKakaoAccount();
          // print('카카오계정으로 로그인 성공');
        } catch (error) {
          // print('카카오계정으로 로그인 실패 $error');
        }
      }
    } else {
      try {
        OAuthToken token = await UserApi.instance.loginWithKakaoAccount();
        // goBack =  asdasd post. {token.accessToken}
        print('카카오계정으로 로그인 성공');
        print("Token : ${token.accessToken}");
      } catch (error) {
        // print('카카오계정으로 로그인 실패 $error');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        backgroundColor: const Color(0xFFF5F5F5),
        fixedSize: const Size(307, 50), // 버튼 크기
      ),
      onPressed: login,
      // 로고와 텍스트를 가로로 나열
      child: Row(
        // 로고와 텍스트를 가운데 정렬
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Image.asset(
            'assets/images/logo/$logo.png',
            width: 18,
          ),
          const SizedBox(width: 10),
          Text(
            whatsLogin,
            style: const TextStyle(
              fontSize: 15,
              color: Color(0xFF49454F),
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }
}
