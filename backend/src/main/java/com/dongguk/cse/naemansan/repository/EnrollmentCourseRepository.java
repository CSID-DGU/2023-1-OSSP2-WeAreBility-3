package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.EnrollmentCourse;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.CourseTagType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface EnrollmentCourseRepository extends JpaRepository<EnrollmentCourse, Long> {
    Optional<EnrollmentCourse> findByIdNotAndTitleAndStatus(Long courseId, String title, Boolean status);
    Optional<EnrollmentCourse> findByIdAndStatus(Long courseId, Boolean Status);
    Optional<EnrollmentCourse> findByTitleAndStatus(String title, Boolean status);

    @Query(value = "SELECT c FROM EnrollmentCourse c WHERE c.user = :user AND c.status = true")
    Page<EnrollmentCourse> findListByUser(User user, Pageable pageable);
    @Query(value = "SELECT c FROM EnrollmentCourse c INNER JOIN Like l WHERE l.user = :user AND c.status = true")
    Page<EnrollmentCourse> findListByLikeAndUser(User user, Pageable pageable);
    @Query(value = "SELECT c FROM EnrollmentCourse c INNER JOIN UsingCourse uc WHERE uc.user = :user AND c.status = true")
    Page<EnrollmentCourse> findListByUsingAndUser(User user, Pageable pageable);
    @Query(value = "SELECT c FROM EnrollmentCourse c INNER JOIN CourseTag t ON c = t.enrollmentCourse WHERE t.courseTagType = :tag AND c.status = true")
    Page<EnrollmentCourse> findListByTag(@Param("tag") CourseTagType courseTagType, Pageable paging);

    @Query(value = "SELECT c FROM EnrollmentCourse c WHERE c.status = true")
    Page<EnrollmentCourse> findListAll(Pageable pageable);
    @Query(value = "SELECT c.id, c.created_date, (SELECT COUNT(*) from likes l WHERE l.course_id = c.id) AS cnt "
            + "FROM enrollment_courses c WHERE c.status = 1 ORDER BY cnt DESC, created_date DESC",
            countQuery = "SELECT c.id, c.created_date, (SELECT COUNT(*) from likes l WHERE l.course_id = c.id) AS cnt "
                    + "FROM enrollment_courses c WHERE c.status = 1 ORDER BY cnt DESC, created_date DESC",
            nativeQuery = true)
    Page<CourseCntForm> findListByLike(Pageable pageable);
    @Query(value = "SELECT c.id, c.created_date, (SELECT COUNT(*) from using_courses u WHERE u.course_id  = c.id) AS cnt "
            + "FROM enrollment_courses c WHERE c.status = 1 ORDER BY cnt DESC, created_date DESC",
            countQuery = "SELECT c.id, c.created_date, (SELECT COUNT(*) from using_courses u WHERE u.course_id  = c.id) AS cnt "
                    + "FROM enrollment_courses c WHERE c.status = 1 ORDER BY cnt DESC, created_date DESC",
            nativeQuery = true)
    Page<CourseCntForm> findListByUsing(Pageable pageable);

    @Query(value = "SELECT c.id AS id, ST_Distance_Sphere(:start, c.start_location) AS radius FROM enrollment_courses c "
            + "WHERE ST_Distance_Sphere(:start, c.start_location) <= 10000 AND c.status = 1",
            countQuery = "SELECT c.id AS id, ST_Distance_Sphere(:start, c.start_location) AS radius FROM enrollment_courses c "
                    + "WHERE ST_Distance_Sphere(:start, c.start_location) <= 10000 AND c.status = 1",
            nativeQuery = true)
    Page<CourseLocationForm> findListByLocation(@Param("start") Point point, Pageable pageable);

//    @Query(value = "SELECT c.title FROM EnrollmentCourse c WHERE c.title = :title AND c.status = :status")
//    Optional<EnrollmentCourse> findByTitleAndStatus(@Param("title") String title, @Param("status") Boolean status);

    public interface CourseLocationForm {
        Long getId();
        Timestamp getCreateDate();
        Double getRadius();
    }

    public interface CourseCntForm {
        Long getId();
        Timestamp getCreateDate();
        Long getCnt();

    }
}

