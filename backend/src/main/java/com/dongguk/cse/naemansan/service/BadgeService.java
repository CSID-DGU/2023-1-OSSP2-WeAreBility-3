package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.dto.response.BadgeDto;
import com.dongguk.cse.naemansan.repository.BadgeRepository;
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
    private final BadgeRepository badgeRepository;
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
}
