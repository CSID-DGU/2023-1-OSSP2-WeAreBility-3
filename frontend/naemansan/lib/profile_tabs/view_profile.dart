//상대방 프로필 조회
//마이페이지와 레이아웃 동일 - 텍스트 버튼 기능은 x (시간 남으면 ...)

import 'package:flutter/material.dart';
import 'package:naemansan/models/other_user_model.dart';
import 'package:naemansan/models/traildetailmodel.dart';
import 'package:naemansan/services/login_api_service.dart';

class ViewProfile extends StatefulWidget {
  final int userId;
  const ViewProfile({Key? key, required this.userId}) : super(key: key);

  @override
  State<ViewProfile> createState() => _ViewProfileState();
}

class _ViewProfileState extends State<ViewProfile> {
  TraildetailModel? trailDetail;
  OtherUserModel? otherUser;
  String imageUrl = "";
  bool? isFollwer = false;

  @override
  void initState() {
    super.initState();
    fetchWriterProfile();
  }

  Future<void> fetchWriterProfile() async {
    ApiService apiService = ApiService();
    Map<String, dynamic>? data =
        await apiService.getOtherUserProfile(widget.userId);
    print("asdasdsadsa");
    Map<String, dynamic>? followerListData = await apiService.getFollower();

    if (followerListData['success'] == true) {
      List<dynamic> dataList = followerListData['data'];

      if (dataList.isEmpty) {
        // 'data'가 비어있는 경우 처리
        print('데이터가 비어있습니다.');
        setState(() {
          isFollwer = false;
        });
      } else {
        // 'data'에 아이템이 있는 경우 처리
        for (var item in dataList) {
          if (item is Map<String, dynamic> && item.containsKey('user_name')) {
            String userName = item['user_name'];

            if (userName == otherUser?.name) {
              // otherUser?.name과 일치하는 user_name이 있는 경우 처리
              print('일치하는 사용자 이름이 있습니다.');
              setState(() {
                isFollwer = true;
              });
            } else {
              setState(() {
                isFollwer = false;
              });
              // otherUser?.name과 일치하지 않는 user_name이 있는 경우 처리
              print('일치하는 사용자 이름이 없습니다.');
            }
          }
        }
      }
    }
    print("asdasdsadsa");
    if (data != null) {
      setState(() {
        otherUser = OtherUserModel.fromJson(data);
        imageUrl =
            'https://ossp.dcs-hyungjoon.com/image?uuid=${otherUser!.imagePath}';
      });
    }
  }

  String fetchUserName(String? name) {
    if (name == null) {
      return 'Loading...';
    } else if (name.length >= 7) {
      return '${name.substring(0, 7)}...';
    } else {
      return name;
    }
  }

  follow() async {
    print("팔로우 신청");
    ApiService apiService = ApiService();
    await apiService.followUser(widget.userId);
    setState(() {
      isFollwer = true;
      // 새로운 데이터를 불러오기 위해 다시 프로필을 가져옵니다.
      fetchWriterProfile();
    });
  }

  unfollow() async {
    print("팔로우 취소");
    ApiService apiService = ApiService();
    await apiService.unfollowUser(widget.userId);
    setState(() {
      isFollwer = false;
      // 새로운 데이터를 불러오기 위해 다시 프로필을 가져옵니다.
      fetchWriterProfile();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: [
                  Text(
                    '${fetchUserName(otherUser?.name)}님의 프로필',
                    style: const TextStyle(
                      fontSize: 21,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const SizedBox(width: 8),
                ],
              ),
            ),
            const Spacer(),
            IconButton(
              icon: Icon(
                isFollwer! ? Icons.person_remove_alt_1 : Icons.person_add_alt_1,
                color: Colors.black,
              ),
              onPressed: () {
                if (isFollwer!) {
                  unfollow();
                } else {
                  follow();
                }
              },
            ),
          ],
        ),
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          await fetchWriterProfile();
        },
        child: SingleChildScrollView(
          child: Column(
            children: [
              Padding(
                padding:
                    const EdgeInsets.symmetric(horizontal: 20, vertical: 50),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    CircleAvatar(
                      radius: 50,
                      backgroundImage: NetworkImage(imageUrl),
                    ),
                    const SizedBox(height: 16),
                    Text(
                      otherUser?.name ?? 'Loading...',
                      style: const TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      otherUser?.introduction ?? 'No Introduction',
                      style: const TextStyle(fontSize: 16),
                    ),
                    const SizedBox(height: 16),
                    const Divider(),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Column(
                          children: [
                            const Text(
                              '팔로워',
                              style: TextStyle(fontSize: 16),
                            ),
                            TextButton(
                              onPressed: () {},
                              child: Text(
                                otherUser?.followerCnt.toString() ??
                                    'Loading...',
                                style: const TextStyle(
                                  color: Colors.black,
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),
                        Column(
                          children: [
                            const Text(
                              '팔로잉',
                              style: TextStyle(fontSize: 16),
                            ),
                            const SizedBox(height: 8),
                            TextButton(
                              onPressed: () {},
                              child: Text(
                                otherUser?.followingCnt.toString() ??
                                    'Loading...',
                                style: const TextStyle(
                                  color: Colors.black,
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                    const SizedBox(height: 16),
                    const Divider(),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Column(
                          children: [
                            const Text(
                              '좋아요한 산책로',
                              style: TextStyle(fontSize: 16),
                            ),
                            const SizedBox(height: 8),
                            TextButton(
                              onPressed: () {},
                              child: Text(
                                otherUser?.likeCnt.toString() ?? 'Loading...',
                                style: const TextStyle(
                                  color: Colors.black,
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),
                        Column(
                          children: [
                            const Text(
                              '작성한 후기',
                              style: TextStyle(fontSize: 16),
                            ),
                            const SizedBox(height: 8),
                            TextButton(
                              onPressed: () {},
                              child: Text(
                                otherUser?.cmtCnt.toString() ?? 'Loading...',
                                style: const TextStyle(
                                  color: Colors.black,
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),
                        Column(
                          children: [
                            const Text(
                              '획득한 뱃지',
                              style: TextStyle(fontSize: 16),
                            ),
                            const SizedBox(height: 8),
                            TextButton(
                              onPressed: () {},
                              child: Text(
                                otherUser?.badgeCnt.toString() ?? 'Loading...',
                                style: const TextStyle(
                                  color: Colors.black,
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
