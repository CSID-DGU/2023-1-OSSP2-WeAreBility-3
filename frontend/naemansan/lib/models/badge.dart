class BadgeModel {
  final int badgeId;
  final String badgeName;
  final DateTime getDate;

  BadgeModel({
    required this.badgeId,
    required this.badgeName,
    required this.getDate,
  });

  factory BadgeModel.fromJson(Map<String, dynamic> json) {
    return BadgeModel(
      badgeId: json['badge_id'] as int,
      badgeName: json['badge_name'] as String,
      getDate: DateTime.parse(json['get_date'] as String),
    );
  }
}
