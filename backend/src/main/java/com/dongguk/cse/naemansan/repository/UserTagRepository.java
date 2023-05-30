package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.UserTag;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    List<UserTag> findByUser(User user);
    void deleteByUserAndTag(User user, CourseTagType tag);
}