class TagModel {
  final String name;

  TagModel({required this.name});

  factory TagModel.fromJson(Map<String, dynamic> json) {
    return TagModel(
      name: json['name'],
    );
  }
}
