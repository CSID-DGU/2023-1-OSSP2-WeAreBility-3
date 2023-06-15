import 'package:flutter/material.dart';
import 'package:naemansan/services/login_api_service.dart';

class SelectTagScreen extends StatefulWidget {
  final bool isEdit;

  const SelectTagScreen({
    required this.isEdit,
    super.key,
  });

  @override
  State<SelectTagScreen> createState() => _SelectTagScreenState();
}

class _SelectTagScreenState extends State<SelectTagScreen> {
  List<String> allTags = [];
  List<Map<String, dynamic>> selectedTags = [];

  @override
  void initState() {
    super.initState();
    print("widget.isEdit 값은? ${widget.isEdit}");
    getTagList();
  }

  Future<void> getTagList() async {
    ApiService apiService = ApiService();
    var data = await apiService.getTagList();

    setState(() {
      allTags = data['data'].cast<String>();
    });
  }

  void toggleTagSelection(String tag) {
    setState(() {
      if (selectedTags.any((element) => element['name'] == tag)) {
        selectedTags.removeWhere((element) => element['name'] == tag);
      } else {
        if (selectedTags.length < 3) {
          selectedTags.add({'name': tag, 'status': 'NEW'});
        }
      }
    });
  }

  goIndex() {
    Navigator.pushNamedAndRemoveUntil(context, '/index', (route) => false);
  }

  void submitTags() async {
    ApiService apiService = ApiService();
    // 서버에 POST 요청을 보내는 로직을 추가해야 합니다.
    // selectedTags 리스트를 서버에 전송
    Map<String, dynamic> tagData = {
      "tags": selectedTags,
    };
    bool? success;
    // 수정 상태

    print("${widget.isEdit} 값은?");
    var myDataTag = await apiService.getMyTag();

    if (myDataTag != null && !myDataTag['data']['tags'].isEmpty) {
      success = await apiService.putMyTag(tagData);
    } else {
      success = await apiService.postMyTag(tagData);
    }

    print(success);
    if (success) {
      goIndex();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: const Text("키워드 선택"),
      ),
      body: Padding(
        padding:
            const EdgeInsets.only(top: 10, left: 20, right: 20, bottom: 10.0),
        child: Column(
          children: [
            const SizedBox(
              height: 30,
            ),
            const Text(
              '추천받길 원하시는 키워드 3가지를 선택해주세요!',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w500,
              ),
            ),
            const SizedBox(
              height: 20,
            ),
            Expanded(
              child: ListView.builder(
                itemCount: allTags.length,
                itemBuilder: (context, index) {
                  final keyword = allTags[index];
                  final isSelected =
                      selectedTags.any((element) => element['name'] == keyword);

                  return ListTile(
                    title: Text(keyword),
                    onTap: () {
                      toggleTagSelection(keyword);
                    },
                    trailing: isSelected ? const Icon(Icons.check) : null,
                    tileColor:
                        isSelected ? Colors.green.withOpacity(0.3) : null,
                  );
                },
              ),
            ),
            ElevatedButton(
              // button white
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.white,
                foregroundColor: Colors.black,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                minimumSize: const Size(300, 50),
              ),
              onPressed: submitTags,
              child: const Text("제출",
                  style: TextStyle(fontSize: 15, fontWeight: FontWeight.w600)),
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
