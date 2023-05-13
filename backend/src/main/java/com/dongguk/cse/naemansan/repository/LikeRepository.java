package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserId(Long id);
    List<Like> findByCourseId(Long id);

    void deleteByUserIdAndCourseId(Long userId, Long courseId);
}
