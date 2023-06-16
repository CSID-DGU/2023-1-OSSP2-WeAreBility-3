# 🌿 내가 만든 산책로, 내만산

## 🤔 This Repo?

양방향 산책로 공유 플랫폼, 내만산 Front Flutter Repository입니다.

## 💻 Dependencies

```yaml
dependencies:
    flutter:
        sdk: flutter
    intl: ^0.18.1
    image_picker: ^0.8.4+4
    cupertino_icons: ^1.0.2
    flutter_native_splash: ^2.2.19
    shared_preferences: ^2.1.0
    google_sign_in: ^6.1.0
    sign_in_with_apple: ^4.3.0
    kakao_flutter_sdk: ^1.4.2
    flutter_dotenv: ^5.0.2
    geolocator: ^9.0.2
    http: ^0.13.6
    permission_handler: ^10.2.0
    carousel_slider: ^4.2.1
    uuid: ^3.0.7
    flutter_web_auth: ^0.5.0
    geocoding: ^2.1.0
    flutter_local_notifications: ^14.0.0+2
    url_launcher: ^6.1.11
    jwt_decoder: ^2.0.1
    webview_flutter: ^3.0.4
    flutter_secure_storage: ^8.0.0
    flutter_webview_plugin: ^0.4.0
    google_maps_flutter: ^2.2.8
    provider: ^6.0.5
    naver_map_plugin:
        git: https://github.com/LBSTECH/naver_map_plugin
    get: ^4.6.5
    firebase_core: ^2.13.1
    firebase_messaging: ^14.6.2
    rflutter_alert: ^2.0.7
    lottie: ^2.3.0
    iamport_flutter: ^0.10.9
    fluttertoast: ^8.2.2
dev_dependencies:
    flutter_test:
        sdk: flutter
    flutter_lints: ^2.0.0
    flutter_launcher_icons: ^0.13.1
```

## 🛠️ How do I build it?

### 0️⃣ 만약 Flutter 기본 셋팅이 안되어 있나요?

그러면 아래 공식 문서를 참고해봐요!

> -   [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
> -   [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)

### 1️⃣ 환경변수 설정

> frontend/naemansan/assets/config/.env 경로에 .env파일 생성

```
YOUR_NATIVE_APP_KEY = "YOUR KAKAO NATIVE APP KEY"
GOOGLE_MAPS_API_KEY = "YOUR GOOGLE MAP API KEY"
NAVER_MAPS_API_KEY = "YOUR NAVER MAP API KEY"
```

를 설정해준다.

만약 없다면 아래 사이트에서 발급 가능하다.

-   구글 맵 : https://developers.google.com/maps?hl=ko
-   카카오 개발자 : https://developers.kakao.com/
-   네이버 클라우드 플랫폼 : https://www.ncloud.com/product/applicationService/maps

### 1️⃣ 실행

```
flutter run // 플러터 실행
```

을 통해서 실행해주자

### 2️⃣ 실행이 안될때

#### 1. flutter ERROR

```dart
flutter clean // 플러터 의존성 제거
flutter pub get  // 의존성 패키지 재설치
```

을 통해서 플러터 의존성을 다시 받아주자.

#### 2. iOS ERROR

```dart
cd ios // ios 폴더 이동
rm -rf Podfile.lock // Podfile.lock 제거
pod install --repo-update // 의존성 패키지 재설치

```
