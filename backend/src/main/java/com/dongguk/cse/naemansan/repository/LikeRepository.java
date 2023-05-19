package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.Like;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByLikeUser(User user);
    List<Like> findByLikeCourse(Course course);

    Optional<Like> findByLikeUserAndLikeCourse(User user, Course course);
    void deleteByLikeUserAndLikeCourse(User user, Course course);
}
