//trailmodel.dart
class KeyWordBoxModel {
  final String tags;

  KeyWordBoxModel({
    required this.tags,
  });

  factory KeyWordBoxModel.fromJson(Map<String, dynamic> json) {
    return KeyWordBoxModel(
      tags: json['name'],
    );
  }
}
