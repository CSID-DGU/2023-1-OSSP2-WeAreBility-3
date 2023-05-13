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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final BadgeNameRepository badgeNameRepository;

    public List<BadgeDto> readBadgeList(Long userid) {
        List<Badge> badges =  badgeRepository.findByUserId(userid);
        List<BadgeName> badgeNames = badgeNameRepository.findAll();

        List<BadgeDto> badgeDtos = new ArrayList<>();
        for (Badge badge : badges) {
            badgeDtos.add(BadgeDto.builder()
                    .badgeId(badge.getBadgeId())
                    .badgeName(getBadgeName(badge.getBadgeId(), badgeNames))
                    .getDate(badge.getGetDate()).build());
        }

        return badgeDtos;
    }

    private String getBadgeName(Long id, List<BadgeName> badgeNames) {
        for (BadgeName badgeName : badgeNames) {
            if (id == badgeName.getId())
                return badgeName.getName();
        }
        return null;
    }
}
