package com.dongguk.cse.naemansan.service;

import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CourseServiceIntegrationTest {
//    @Autowired
//    CourseService courseService;
//    @Autowired
//    CourseRepository courseRepository;
//
//    @Test
//    void 산책로추가() {
//        Course course = new Course();
//        Date date = new Date();
//        List<Point> listp = new ArrayList<>();
//        Point point1 = new Point(37.56394076405744, 126.97730616581127);
//        Point point2 = new Point(38.56394076405744, 127.97730616581127);
//        Point point3 = new Point(39.56394076405744, 129.97730616581127);
//        listp.add(point1);
//        listp.add(point2);
//        listp.add(point3);
//        String tag = "ABC";
//        course.setUser_id(1);
//        course.setTitle("test1 title");
//        course.setCreated_date(date);
//        course.setIntroduction("introduction test1");
//        course.setStart_location("start location test1");
//        course.setLocations(listp);
//        course.setDistance(10);
//        course.setStatus(13);
//
//        //join으로 course, courseType 확인
//        CourseTag courseTag = new CourseTag();
//        courseTag.setCourseTag(tag);
//        Long saveId = courseService.join(course, courseTag);
//       // Long saveTagId = courseService.joinTag(courseType);
//        Course findCourse = courseService.findOne(saveId).get();
//        assertThat(course.getId()).isEqualTo(findCourse.getId());
//    }
//
//    @Test
//    void 모든산책로조회() {
//        List<Course> courseList = courseService.findCourses();
//        for (Course course : courseList) {
//            System.out.println(course.getId());
//            System.out.println(course.getTitle());
//            System.out.println(course.getIntroduction());
//            System.out.println(course.getStart_location());
//        }
//    }
//
//    @Test
//    void 산책로수정() {
//        Course course = courseService.findOneTitle("test1 title").get();
//        course.setIntroduction("introduction test modify2");
//        course.setStart_location("start location test modify2");
//        course.setDistance(10);
//        course.setStatus(13);
//        Long saveId = courseService.update(course);
//        Course findCourse = courseService.findOne(saveId).get();
//        assertThat(course.getId()).isEqualTo(findCourse.getId());
//    }
//
//    @Test
//    void 산책로제목조회() {
//        Course course = courseService.findOneTitle("test1 title").get();
//        System.out.println(course.getId());
//        System.out.println(course.getTitle());
//        System.out.println(course.getIntroduction());
//        System.out.println(course.getStart_location());
//    }
//
//    @Test
//    void 산책로삭제(){
//        Course course = courseService.findOneTitle("test1 title").get();
//        courseService.delete(course);
//    }
}
