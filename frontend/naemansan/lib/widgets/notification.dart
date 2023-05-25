import 'package:flutter/material.dart';

class NotificationIcon extends StatelessWidget {
  final bool hasUnreadNotifications;
  final VoidCallback onPressed;

  const NotificationIcon({
    super.key,
    required this.hasUnreadNotifications,
    required this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.topRight,
      children: [
        IconButton(
          icon: const Icon(
            Icons.notifications_none_rounded,
            color: Colors.black,
          ),
          onPressed: onPressed,
        ),
        if (hasUnreadNotifications)
          Positioned(
            right: 12,
            top: 12,
            child: Container(
              width: 10,
              height: 10,
              decoration: const BoxDecoration(
                shape: BoxShape.circle,
                color: Colors.red,
              ),
            ),
          ),
      ],
    );
  }
}
