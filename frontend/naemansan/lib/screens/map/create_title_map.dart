import 'package:flutter/material.dart';
import 'package:naemansan/services/login_api_service.dart';
import 'package:naemansan/tabs/tab_myrail.dart';

class CreateTitleScreen extends StatefulWidget {
  const CreateTitleScreen({Key? key}) : super(key: key);

  @override
  _CreateTitleScreenState createState() => _CreateTitleScreenState();
}

class _CreateTitleScreenState extends State<CreateTitleScreen> {
  final _formKey = GlobalKey<FormState>();
  final _titleController = TextEditingController();

  @override
  void dispose() {
    _titleController.dispose();
    super.dispose();
  }

  void sendCourseData() async {
    ApiService apiService = ApiService();

    final title = _titleController.text;
    final locations =
        ModalRoute.of(context)!.settings.arguments as List<Map<String, double>>;
    print("locations가 이렇게 들어가있어요! $locations");

    final courseData = {
      'title': title,
      'locations': locations,
    };

    print("--------------------산책로 생성한다--------------------");
    final response = await apiService.registerIndividualCourse(courseData);
    print(response);
    print("--------------------산책로 생성한다--------------------");
    if (response['success']) {
      final data = response['data'];
      final id = data['id'];
      final title = data['title'];
      final locations = data['locations'];
      final createDate = data['create_date'];
      final distance = data['distance'];
      print(data);
      // 응답 데이터를 활용한 작업 수행
    } else {
      final error = response['error'];

      // 오류 처리
    }
  }

  @override
  Widget build(BuildContext context) {
    final locations = ModalRoute.of(context)!.settings.arguments;
    print("우어어엉?$locations");

    return Scaffold(
      appBar: AppBar(
        // back x
        automaticallyImplyLeading: false,

        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: const Text('산책로 이름을 입력해주세요!'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              TextFormField(
                controller: _titleController,
                decoration: const InputDecoration(labelText: '산책로 이름'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '산책로 이름을 입력해주세요!';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
              // 제출 버튼
              ElevatedButton(
                onPressed: () async {
                  if (_formKey.currentState!.validate()) {
                    sendCourseData();
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const Myrail()),
                    );
                  }
                },
                child: const Text('산책로 만들기'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
