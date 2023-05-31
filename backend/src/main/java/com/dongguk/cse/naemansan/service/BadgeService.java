package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Badge;
import com.dongguk.cse.naemansan.domain.BadgeName;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.BadgeType;
import com.dongguk.cse.naemansan.dto.response.BadgeDto;
import com.dongguk.cse.naemansan.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BadgeService {
    private final UserRepository userRepository;
    private final IndividualCourseRepository individualCourseRepository;
    private final EnrollmentCourseRepository enrollmentCourseRepository;
    private final UsingCourseRepository usingCourseRepository;
    private final CommentRepository commentRepository;
    private final BadgeRepository badgeRepository;
    private final BadgeNameRepository nameRepository;

    public List<BadgeDto> readBadgeList(Long userid) {
        // 해당 유저가 가진 Badge 조회
        List<Object[]> badges = badgeRepository.findUserBadgeList(userid);

        // Dto 변환
        List<BadgeDto> badgeDtoList = new ArrayList<>();
        for (Object[] objects : badges) {
            badgeDtoList.add(BadgeDto.builder()
                    .badge_id((Long) objects[0])
                    .badge_name(objects[1].toString())
                    .get_date((Timestamp) objects[2]).build());
        }

        // Dto 반환
        return badgeDtoList;
    }

    public void createIndividualBadge(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<BadgeName> badges = nameRepository.findByTypeOrderByConditionNumDesc(BadgeType.INDIVIDUAL);
        Long numByUser = individualCourseRepository.countByUser(user);

        BadgeName willMakeBadge = null;
        for (BadgeName badge : badges) {
            if (badge.getConditionNum() != numByUser) { continue; }
            willMakeBadge = badge;
            break;
        }

        if (willMakeBadge != null) {
            badgeRepository.save(Badge.builder()
                    .user(user)
                    .badgeName(willMakeBadge).build());
        }
    }

    public void createEnrollmentBadge(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<BadgeName> badges = nameRepository.findByTypeOrderByConditionNumDesc(BadgeType.ENROLLMENT);
        Long numByUser = enrollmentCourseRepository.countByUser(user);

        BadgeName willMakeBadge = null;
        for (BadgeName badge : badges) {
            if (badge.getConditionNum() != numByUser) { continue; }
            willMakeBadge = badge;
            break;
        }

        if (willMakeBadge != null) {
            badgeRepository.save(Badge.builder()
                    .user(user)
                    .badgeName(willMakeBadge).build());
        }
    }

    public void createUsingBadge(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<BadgeName> badges = nameRepository.findByTypeOrderByConditionNumDesc(BadgeType.USING);
        Long numByUser = usingCourseRepository.countByUser(user);

        BadgeName willMakeBadge = null;
        for (BadgeName badge : badges) {
            if (badge.getConditionNum() != numByUser) { continue; }
            willMakeBadge = badge;
            break;
        }

        if (willMakeBadge != null) {
            badgeRepository.save(Badge.builder()
                    .user(user)
                    .badgeName(willMakeBadge).build());
        }
    }

    public void createCommentBadge(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        List<BadgeName> badges = nameRepository.findByTypeOrderByConditionNumDesc(BadgeType.COMMENT);
        Long numByUser = commentRepository.countByUser(user);

        BadgeName willMakeBadge = null;
        for (BadgeName badge : badges) {
            if (badge.getConditionNum() != numByUser) { continue; }
            willMakeBadge = badge;
            break;
        }

        if (willMakeBadge != null) {
            badgeRepository.save(Badge.builder()
                    .user(user)
                    .badgeName(willMakeBadge).build());
        }
    }
}
