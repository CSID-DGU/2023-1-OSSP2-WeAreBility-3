package com.dongguk.cse.naemansan.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UsingCourseBadgeEvent {
    private Long userId;
}