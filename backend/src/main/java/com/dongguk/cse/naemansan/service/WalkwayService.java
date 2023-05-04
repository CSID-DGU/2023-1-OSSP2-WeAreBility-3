package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.domain.Walkway;
import com.dongguk.cse.naemansan.repository.Walkwayrepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class WalkwayService {
    private final Walkwayrepository walkwayrepository;

    public WalkwayService(Walkwayrepository walkwayrepository) {
        this.walkwayrepository = walkwayrepository;
    }
    //산책도 등록
    public int join(Walkway walkway){
        validateDuplicateWalkway(walkway);//중복 검사
        walkwayrepository.save(walkway);
        return walkway.getId();
    }
    //산책로 중복 검사 (이름으로 바꿔야 할 수도?)
    private void validateDuplicateWalkway(Walkway walkway){
            walkwayrepository.findById(walkway.getId())
                    .ifPresent(w -> {
                        throw  new IllegalStateException("이미 존재하는 산책로 입니다");
                    });
    }
    //전체 산책로 조회
    public List<Walkway> findWalkways(){
        return walkwayrepository.findAll();
    }
    //키워드 기반 조회
    public Optional<Walkway> OrderbyKeword(String tag) {
        return walkwayrepository.orderByKeyword(tag);
    }
}
