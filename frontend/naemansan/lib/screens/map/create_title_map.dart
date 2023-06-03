import 'package:flutter/material.dart';

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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Enter Walking Path Title'),
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
                decoration: const InputDecoration(labelText: 'Title'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a title';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
              // ElevatedButton(
              //   onPressed: () {
              //     if (_formKey.currentState?.validate() == true) {
              //       final enteredTitle = _titleController.text;
              //       final walkingPath = {
              //         "title": enteredTitle,
              //         "locations": _pathOverlays.toList().map((pathOverlay) {
              //           return {
              //             "latitude": pathOverlay.coords[0].latitude,
              //             "longitude": pathOverlay.coords[0].longitude,
              //           };
              //         }).toList(),
              //       };

              //       Navigator.pop(context, walkingPath);
              //     }
              //   },
              //   child: const Text('Submit'),
              // ),
            ],
          ),
        ),
      ),
    );
  }
}
