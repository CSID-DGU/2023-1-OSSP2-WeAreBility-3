// import 'package:flutter/foundation.dart';
// import 'package:geolocator/geolocator.dart';

// class Keyword with ChangeNotifier {
//   List<bool> keywordButtonStates = [
//     true,
//     false,
//     false,
//     false,
//   ]; // Initial button states

//   Position? get currentPosition => _currentPosition;

//   Future<void> updateCurrentPosition() async {
//     try {
//       final permission = await Geolocator.checkPermission();
//       if (permission == LocationPermission.denied) {
//         final requestedPermission = await Geolocator.requestPermission();
//         if (requestedPermission == LocationPermission.denied) {
//           throw Exception('위치 권한이 없습니다.');
//         }
//       }
//       if (permission == LocationPermission.deniedForever) {
//         throw Exception('위치 권한이 영구적으로 없습니다.');
//       }

//       final position = await Geolocator.getCurrentPosition(
//         desiredAccuracy: LocationAccuracy.high,
//       );
//       _currentPosition = position;
//       notifyListeners();
//     } catch (error) {
//       print('Error fetching location: $error');
//     }
//   }
// }
