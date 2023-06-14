import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/services/login_api_service.dart';

class CourseEditpage extends StatefulWidget {
  final int id;
  final String title;
  final String introduction;
  final List<String> keywords;

  const CourseEditpage({
    Key? key,
    required this.id,
    required this.title,
    required this.introduction,
    required this.keywords,
  }) : super(key: key);

  @override
  State<CourseEditpage> createState() => _CourseEditpageState();
}

class _CourseEditpageState extends State<CourseEditpage> {
  static const storage = FlutterSecureStorage();

  String newTitle = '';
  String newIntro = '';
  List<Map<String, String>> keywordStatus = [];

  // 산책로 정보 업데이트
  Future<void> saveChanges() async {
    final apiService = ApiService();
    final tags = keywordStatus
        .map((item) => {
              'name': item['name']!,
              'status': item['status']!,
            })
        .toList();
    final response = await apiService.putRequest(
        'course/enrollment/${widget.id}',
        {'title': newTitle, 'introduction': newIntro, 'tags': tags});
    // 산책로 정보 다시 불러오기
  }

  @override
  void initState() {
    super.initState();
    newIntro = widget.introduction;
    newTitle = widget.title;

    // Initialize keyword status
    for (final keyword in widget.keywords) {
      keywordStatus.add({
        'name': keyword,
        'status': 'DEFAULT',
      });
    }
  }

  void toggleKeywordStatus(String keyword) {
    setState(() {
      for (final item in keywordStatus) {
        if (item['name'] == keyword) {
          if (item['status'] == 'DEFAULT') {
            item['status'] = 'DELETE';
          } else if (item['status'] == 'DELETE') {
            item['status'] = 'DEFAULT';
          }
          break;
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        titleSpacing: 0,
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back_ios_outlined,
            color: Colors.black,
          ),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        title: Row(
          children: [
            const Padding(
              padding: EdgeInsets.only(left: 5.0),
              child: Text(
                '산책로 수정',
                style: TextStyle(
                  fontSize: 21,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const Spacer(),
            TextButton(
              onPressed: () {
                saveChanges();
                Navigator.of(context).pop(); // 이전 페이지 (공개 산책로 디테일 페이지)로 돌아가기
              },
              child: const Text(
                '완료', // 완료 버튼을 누르면 모두 저장
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 20.0,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            const SizedBox(width: 6),
          ],
        ),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 25),
              child: Column(
                children: [
                  const Divider(
                    thickness: 1,
                  ),
                  Row(
                    children: [
                      Expanded(
                        child: TextFormField(
                          initialValue: widget.title,
                          onChanged: (value) {
                            // 산책로 제목 업데이트
                            setState(() {
                              newTitle = value;
                            });
                          },
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                          textAlign: TextAlign.center,
                          decoration: const InputDecoration(
                            hintText: '제목',
                            border: InputBorder.none,
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 25),
              child: Column(
                children: [
                  const Divider(
                    thickness: 1,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Expanded(
                        child: TextFormField(
                          initialValue: widget.introduction,
                          onChanged: (value) {
                            // 산책로 소개 업데이트
                            setState(() {
                              newIntro = value;
                            });
                          },
                          style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                          textAlign: TextAlign.center,
                          decoration: const InputDecoration(
                            hintText: '소개',
                            border: InputBorder.none,
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),
            // Keyword Selection
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 25),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Divider(
                    thickness: 1,
                  ),
                  const SizedBox(height: 10),
                  const Text(
                    '키워드',
                    style: TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Wrap(
                    spacing: 8.0,
                    runSpacing: 8.0,
                    children: widget.keywords
                        .map(
                          (keyword) => GestureDetector(
                            onTap: () => toggleKeywordStatus(keyword),
                            child: Container(
                              padding: const EdgeInsets.symmetric(
                                  vertical: 4.0, horizontal: 10.0),
                              decoration: BoxDecoration(
                                color: keywordStatus.firstWhere((item) =>
                                            item['name'] ==
                                            keyword)['status'] ==
                                        'DEFAULT'
                                    ? Colors.blue
                                    : Colors.black,
                                borderRadius: BorderRadius.circular(16.0),
                              ),
                              child: Text(
                                keyword,
                                style: const TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.white,
                                ),
                              ),
                            ),
                          ),
                        )
                        .toList(),
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
