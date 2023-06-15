import 'package:flutter/material.dart';
import 'package:naemansan/services/login_api_service.dart';

class CreateErollmentCourseScreen extends StatefulWidget {
  const CreateErollmentCourseScreen({super.key});

  @override
  State<CreateErollmentCourseScreen> createState() =>
      _CreateErollmentCourseScreenState();
}

class _CreateErollmentCourseScreenState
    extends State<CreateErollmentCourseScreen> {
  List<String> allTags = [];
  List<String> selectedTags = [];
  TextEditingController introductionController = TextEditingController();
  @override
  void initState() {
    super.initState();

    getTagList();
  }

  Future<void> getTagList() async {
    ApiService apiService = ApiService();
    var data = await apiService.getTagList();

    setState(() {
      allTags = data['data'].cast<String>();
    });
  }

  void submitEnrollmentCourse() {
    final String introduction = introductionController.text;

    if (introduction.isEmpty) {
      print("내용을 입력해주세요");
      return;
    }
    final arguments =
        ModalRoute.of(context)?.settings.arguments as Map<String, Object>;
    final id = arguments['id'];
    final title = arguments['title'];

    final Map<String, dynamic> data = {
      'individual_id': id, // Replace with the actual individual ID
      'title': title,
      'introduction': introduction,
      'tags':
          selectedTags.map((tag) => {'name': tag, 'status': 'NEW'}).toList(),
    };

    print(data);

    // Perform the submission
    // ...
  }

  @override
  Widget build(BuildContext context) {
    final arguments =
        ModalRoute.of(context)?.settings.arguments as Map<String, Object>;
    final id = arguments['id'];
    final title = arguments['title'];

    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(15.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 70),
            Text("Title: $title",
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.w600,
                )),
            const SizedBox(height: 16),
            Container(
              padding: const EdgeInsets.all(16),
              child: TextField(
                controller: introductionController,
                maxLines: 5,
                decoration: const InputDecoration(
                  hintText: 'Enter Introduction',
                  border: OutlineInputBorder(),
                ),
              ),
            ),
            const SizedBox(height: 16),
            const Text("키워드를 선택해주세요!(최대 8개)",
                style: TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.w600,
                )),
            Expanded(
              child: ListView.builder(
                itemCount: allTags.length,
                itemBuilder: (context, index) {
                  final tag = allTags[index];
                  return CheckboxListTile(
                    title: Text(tag),
                    value: selectedTags.contains(tag),
                    onChanged: (value) {
                      setState(() {
                        if (value == true) {
                          if (selectedTags.length < 8) {
                            selectedTags.add(tag);
                          }
                        } else {
                          selectedTags.remove(tag);
                        }
                      });
                    },
                  );
                },
              ),
            ),
            Row(
              children: [
                const SizedBox(width: 30),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.white,
                    foregroundColor: Colors.black,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                    minimumSize: const Size(300, 50),
                  ),
                  onPressed: () {
                    showDialog(
                      context: context,
                      builder: (context) {
                        return AlertDialog(
                          title: const Text("Confirmation"),
                          content: const Text("공개하시겠습니까?"),
                          actions: [
                            TextButton(
                              onPressed: () {
                                if (selectedTags.isNotEmpty) {
                                  submitEnrollmentCourse(); // Call the submit function
                                  Navigator.pop(context); // Close the dialog
                                } else {
                                  // Display an error message or take appropriate action
                                }
                              },
                              child: const Text("네",
                                  style: TextStyle(color: Colors.black)),
                            ),
                            TextButton(
                              onPressed: () {
                                Navigator.pop(context); // Close the dialog
                              },
                              child: const Text("아니오",
                                  style: TextStyle(color: Colors.black)),
                            ),
                          ],
                        );
                      },
                    );
                  },
                  child: const Text("공개하기",
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w600,
                      )),
                ),
              ],
            ),
            const SizedBox(
              height: 20,
            )
          ],
        ),
      ),
    );
  }
}
