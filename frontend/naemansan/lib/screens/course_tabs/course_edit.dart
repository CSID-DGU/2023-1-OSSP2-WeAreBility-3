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
  List<String> allKeywords = [
    '강남구',
    '강동구',
    '강북구',
    '강서구',
    '관악구',
    '광진구',
    '구로구',
    '금천구',
    '노원구',
    '도봉구',
    '동대문구',
    '동작구',
    '마포구',
    '서대문구',
    '서초구',
    '성동구',
    '성북구',
    '송파구',
    '양천구',
    '영등포구',
    '용산구',
    '은평구',
    '종로구',
    '중구',
    '중랑구',
    '서울숲',
    '경복궁',
    '성수',
    '한옥마을',
    '핫플',
    '스타벅스',
    '힐링',
    '자연',
    '오솔길',
    '도심',
    '출근길',
    '퇴근길',
    '점심시간',
    '스트레스해소',
    '한강',
    '공원',
    '바다',
    '해안가',
    '개울가',
    '계곡',
    '들판',
    '산',
    '동산',
    '숲길',
    '러닝',
    '맛집',
    '카페',
    '영화',
    '문화',
    '사색',
    '문화재',
    '강아지',
    '고양이',
    '야경',
    '노을',
  ];
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
      {
        'title': newTitle,
        'introduction': newIntro,
        'tags': tags,
      },
    );
    // 산책로 정보 다시 불러오기
  }

  @override
  void initState() {
    super.initState();
    newIntro = widget.introduction;
    newTitle = widget.title;
    // Initialize keyword status
    for (final keyword in allKeywords) {
      final status = widget.keywords.contains(keyword) ? 'DEFAULT' : 'DELETE';
      keywordStatus.add({
        'name': keyword,
        'status': status,
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
            item['status'] = 'NEW';
          } else if (item['status'] == 'NEW') {
            item['status'] = 'DELETE';
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
                Navigator.of(context).pop(); // 이전 페이지로 이동
              },
              child: const Text(
                '저장',
                style: TextStyle(
                  fontSize: 16,
                  color: Colors.black,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
          ],
        ),
      ),
      body: ListView(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        children: [
          const SizedBox(height: 16.0),
          const SizedBox(height: 16.0),
          const Text(
            '제목',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.w600,
            ),
          ),
          const SizedBox(height: 8.0),
          TextFormField(
            initialValue: widget.title,
            onChanged: (value) {
              setState(() {
                newTitle = value;
              });
            },
            decoration: const InputDecoration(
              border: OutlineInputBorder(),
              hintText: '산책로 제목을 입력하세요.',
            ),
          ),
          const SizedBox(height: 16.0),
          const Text(
            '소개',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.w600,
            ),
          ),
          const SizedBox(height: 8.0),
          TextFormField(
            initialValue: widget.introduction,
            onChanged: (value) {
              setState(() {
                newIntro = value;
              });
            },
            maxLines: 5,
            decoration: const InputDecoration(
              border: OutlineInputBorder(),
              hintText: '산책로에 대한 소개를 입력하세요.',
            ),
          ),
          const SizedBox(height: 16.0),
          const Text(
            '키워드',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.w600,
            ),
          ),
          const SizedBox(height: 8.0),
          Wrap(
            spacing: 8.0,
            runSpacing: 4.0,
            children: allKeywords.map((keyword) {
              final keywordItem = keywordStatus.firstWhere(
                (item) => item['name'] == keyword,
                orElse: () => {'name': keyword, 'status': 'DELETE'},
              );
              final isSelected = (keywordItem['status'] == 'DEFAULT' ||
                  keywordItem['status'] == 'NEW');
              return ChoiceChip(
                label: Text(keyword),
                selected: isSelected,
                onSelected: (isSelected) {
                  toggleKeywordStatus(keyword);
                },
                selectedColor: isSelected ? Colors.green : null,
                labelStyle: TextStyle(
                  fontSize: 14.0,
                  fontWeight: FontWeight.w600,
                  color: isSelected ? Colors.white : Colors.black,
                ),
              );
            }).toList(),
          ),
        ],
      ),
    );
  }
}
