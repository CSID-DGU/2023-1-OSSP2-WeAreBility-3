package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Badge;
import com.dongguk.cse.naemansan.domain.BadgeName;
import com.dongguk.cse.naemansan.dto.response.BadgeDto;
import com.dongguk.cse.naemansan.repository.BadgeNameRepository;
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
        log.info("Having Badge List Read - UserID = {}", userid);
        List<Object[]> badges = badgeRepository.findUserBadgeList(userid);

        List<BadgeDto> badgeDtos = new ArrayList<>();

        for (Object[] objects : badges) {
            badgeDtos.add(BadgeDto.builder()
                    .badgeId((Long) objects[0])
                    .badgeName(objects[1].toString())
                    .getDate((Timestamp) objects[2]).build());
        }

        return badgeDtos;
    }
}
