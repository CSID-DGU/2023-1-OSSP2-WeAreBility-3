package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Comment;
import com.dongguk.cse.naemansan.domain.Course;
import com.dongguk.cse.naemansan.domain.Like;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentUser(User user);
    List<Comment> findByCommentCourse(Course course);

    Optional<Comment> findByIdAndCommentUserAndCommentCourse(Long id, User user, Course course);
}
