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

  @override
  Widget build(BuildContext context) {
    final arguments =
        ModalRoute.of(context)?.settings.arguments as Map<String, Object>;
    final id = arguments['id'];
    final title = arguments['title'];

    return Scaffold(
      body: Column(
        children: [
          Text("ID: $id"),
          Text("Title: $title"),
          const SizedBox(height: 16),
          Container(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: introductionController,
              maxLines: 5, // Adjust the number of lines as needed
              decoration: const InputDecoration(
                hintText: 'Enter Introduction',
                border: OutlineInputBorder(),
              ),
            ),
          ),
          const SizedBox(height: 16),
          const Text("Select Keywords:"),
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
                            // Perform the submit action
                            Navigator.pop(context); // Close the dialog
                          } else {
                            // Display an error message or take appropriate action
                          }
                        },
                        child: const Text("네"),
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.pop(context); // Close the dialog
                        },
                        child: const Text("아니오"),
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
    );
  }
}
