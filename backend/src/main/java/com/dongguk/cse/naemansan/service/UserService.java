package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.*;
import com.dongguk.cse.naemansan.domain.type.StatusType;
import com.dongguk.cse.naemansan.dto.response.CommentDto;
import com.dongguk.cse.naemansan.dto.CourseTagDto;
import com.dongguk.cse.naemansan.dto.PointDto;
import com.dongguk.cse.naemansan.dto.response.UserDto;
import com.dongguk.cse.naemansan.dto.request.UserRequestDto;
import com.dongguk.cse.naemansan.dto.response.CourseDto;
import com.dongguk.cse.naemansan.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.MultiPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SubscribeRepository subscribeRepository;

    public UserDto readUserProfile(Long userId) {
        log.info("getUserInformation - ID : {}", userId);

        Optional<User> user = userRepository.findById(userId);
        Optional<Subscribe> subscribe = subscribeRepository.findBySubscribeUser(user.get());


        UserDto userDto = null;
        try {
            if (user.isEmpty())
                throw new NullPointerException();
            else
                userDto = UserDto.builder()
                        .user(user.get())
                        .image(user.get().getImage())
                        .isPremium(subscribe.isEmpty() ? false : true)
                        .commentCnt((long) user.get().getComments().size())
                        .likeCnt((long) user.get().getLikes().size())
                        .badgeCnt((long) user.get().getBadges().size())
                        .followingCnt((long) user.get().getFollowings().size())
                        .followerCnt((long) user.get().getFollowers().size())
                        .build();
        } catch (Exception e) {
            log.info("{}", e.getMessage());
        }

        return userDto;
    }

    @Transactional
    public UserDto updateUserProfile(Long userId, UserRequestDto userRequestDto) {
        log.info("updateUserInformation - {}", userRequestDto);
        Optional<User> user = userRepository.findById(userId);
        Optional<Image> image = imageRepository.findByImageUser(user.get());
        Optional<Subscribe> subscribe = subscribeRepository.findBySubscribeUser(user.get());
        if (user.isEmpty() || image.isEmpty()){
            return null;
        }
        else {
            user.get().updateUser(userRequestDto.getName(), userRequestDto.getInformation());
//            image.get().setImagePath(userRequestDto.getImagePath());
            // 이미지는 따로 한번 더 요청하는 것을 생각 중
            return UserDto.builder()
                    .user(user.get())
                    .image(user.get().getImage())
                    .isPremium(subscribe.isEmpty() ? false : true)
                    .commentCnt((long) user.get().getComments().size())
                    .likeCnt((long) user.get().getLikes().size())
                    .badgeCnt((long) user.get().getBadges().size())
                    .followingCnt((long) user.get().getFollowings().size())
                    .followerCnt((long) user.get().getFollowers().size())
                    .build();
        }
    }

    public Boolean deleteUserInformation(Long id) {
        try {
            userRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Boolean.FALSE;
    }

    public List<CommentDto> readCommentList(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);

        if (findUser.isEmpty()){
            return null;
        }

        List<Comment> commentList = findUser.get().getComments();
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment: commentList) {
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .userId(comment.getCommentUser().getId())
                    .courseId(comment.getCommentCourse().getId())
                    .userName(comment.getCommentUser().getName())
                    .content(comment.getContent())
                    .createdDateTime(comment.getCreatedDate())
                    .isEdit(comment.getIsEdit()).build());
        }

        return commentDtoList;
    }

    public List<CourseDto> readLikeCourseList(Long userId) {
        log.info("User 찾는 중");
        Optional<User> findUser = userRepository.findById(userId);

        if (findUser.isEmpty()){
            return null;
        }

        log.info("Like한 Course List 찾는 중");
        List<Like> likeList = findUser.get().getLikes();

        List<CourseDto> courseDtos = new ArrayList<>();
        for (Like like : likeList) {
            log.info("Like Course 찾는 중");
            Course course = like.getLikeCourse();
            List<PointDto> pointDtoList = getPoint2PointDto(course.getLocations());
            List<CourseTagDto> courseTags = getTag2TagDto(course.getCourseTags());

            courseDtos.add(CourseDto.builder()
                    .id(course.getId())
                    .userId(course.getCourseUser().getId())
                    .userName(course.getCourseUser().getName())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .introduction(course.getIntroduction())
                    .courseTags(courseTags)
                    .startLocationName(course.getStartLocationName())
                    .locations(pointDtoList).build());
            }

        return courseDtos;
    }

    public List<CourseDto> readEnrollmentCourseList(Long userId) {
        log.info("User 찾는 중");
        Optional<User> findUser = userRepository.findById(userId);

        if (findUser.isEmpty()){
            return null;
        }

        List<Course> courseList = findUser.get().getCourses();

        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courseList) {
            List<PointDto> pointDtoList = getPoint2PointDto(course.getLocations());
            List<CourseTagDto> courseTags = getTag2TagDto(course.getCourseTags());

            courseDtos.add(CourseDto.builder()
                    .id(course.getId())
                    .userId(course.getCourseUser().getId())
                    .userName(course.getCourseUser().getName())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .introduction(course.getIntroduction())
                    .courseTags(courseTags)
                    .startLocationName(course.getStartLocationName())
                    .locations(pointDtoList).build());
        }

        return courseDtos;
    }

    public List<CourseDto> readFinishCourseList(Long userId) {
        log.info("User 찾는 중");
        Optional<User> findUser = userRepository.findById(userId);

        if (findUser.isEmpty()){
            return null;
        }

        List<UsingCourse> usingCourseList = findUser.get().getUsingCourses();

        List<CourseDto> courseDtos = new ArrayList<>();
        for (UsingCourse usingCourse : usingCourseList) {
            if (!usingCourse.getFinishStatus()) {
                continue;
            }

            Course course = usingCourse.getCourse();

            List<PointDto> pointDtoList = getPoint2PointDto(course.getLocations());
            List<CourseTagDto> courseTags = getTag2TagDto(course.getCourseTags());

            courseDtos.add(CourseDto.builder()
                    .id(course.getId())
                    .userId(course.getCourseUser().getId())
                    .userName(course.getCourseUser().getName())
                    .title(course.getTitle())
                    .createdDateTime(course.getCreatedDate())
                    .introduction(course.getIntroduction())
                    .courseTags(courseTags)
                    .startLocationName(course.getStartLocationName())
                    .locations(pointDtoList).build());
        }

        return courseDtos;
    }


    private List<CourseTagDto> getTag2TagDto(List<CourseTag> tagList) {
        List<CourseTagDto> dtoList = new ArrayList<>();

        for (CourseTag courseTag : tagList) {
            dtoList.add(CourseTagDto.builder()
                    .courseTagType(courseTag.getCourseTagType())
                    .statusType(StatusType.DEFAULT).build());
        }

        return dtoList;
    }

    private List<PointDto> getPoint2PointDto(MultiPoint multiPoint) {
        List<PointDto> locations = new ArrayList<>();

        for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
            locations.add(new PointDto(multiPoint.getGeometryN(i).getCoordinate().getY(),
                    multiPoint.getGeometryN(i).getCoordinate().getX()));
        }

        return locations;
    }
}
