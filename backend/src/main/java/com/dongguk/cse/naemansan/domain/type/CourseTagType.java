package com.dongguk.cse.naemansan.domain.type;

public enum CourseTagType {
    힐링, 스타벅스, 자연, 오솔길, 도심, 출근길, 퇴근길, 점심시간, 스트레스해소,
    한강, 공원, 성수, 강아지, 바다, 해안가, 러닝, 맛집, 카페, 영화, 문화, 사색,
    핫플, 서울숲, 경복궁, 한옥마을, 문화재, 고양이, 개울가, 계곡, 들판, 산, 동산, 야경, 노을, 숲길,
    강서구, 양천구, 구로구, 영등포구, 금천구, 동작구, 관악구, 서초구, 강남구, 송파구, 강동구,
    은평구, 서대문구, 마포구, 용산구, 중구, 종로구, 도봉구, 강북구, 성북구, 동대문구, 성동구, 노원구, 광진구;

    public static CourseTagType existType(String type) { // 매개변수가 Enum 상수에 존재하는지 확인
        for (CourseTagType courseTagType : CourseTagType.values()) {
            if ((type.equals(courseTagType.toString())))
                return courseTagType;
        }
        return null;
    }
}
