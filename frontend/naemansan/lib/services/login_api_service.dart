// import 'dart:convert';

// import 'package:http/http.dart' as http;

// void getUserInfo() async {
//   var url = Uri.parse('백 서버 주소');
//   var headers = {'Authorization': 'Bearer $accessToken'}; // 액세스 토큰으로 대체해야 함

//   var response = await http.get(url, headers: headers);

//   // 서버로부터의 응답 처리
//   if (response.statusCode == 200) {
//     // 사용자 정보 성공적으로 가져옴
//     var userInfo = jsonDecode(response.body);
//     // ...
//   } else {
//     // 사용자 정보 가져오기 실패
//   }
// }
