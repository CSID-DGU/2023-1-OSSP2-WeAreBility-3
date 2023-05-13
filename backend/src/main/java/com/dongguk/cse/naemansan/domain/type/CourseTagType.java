package com.dongguk.cse.naemansan.domain.type;

import java.util.Arrays;

public enum CourseTagType {
    ABC, DEF, GHI, JKL, MNO, PQR, STU, VWU, XYZ;

    public static CourseTagType existType(String type) { // 매개변수가 Enum 상수에 존재하는지 확인
        for (CourseTagType courseTagType : CourseTagType.values()) {
            if ((type.equals(courseTagType.toString())))
                return courseTagType;
        }
        return null;
    }
}
