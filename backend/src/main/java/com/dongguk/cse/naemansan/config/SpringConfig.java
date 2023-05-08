package com.dongguk.cse.naemansan.config;

import com.dongguk.cse.naemansan.repository.CourseTypeRepository;
import com.dongguk.cse.naemansan.repository.JpaCourseRepository;
import com.dongguk.cse.naemansan.repository.CourseRepository;
import com.dongguk.cse.naemansan.repository.JpaCourseTypeRepository;
import com.dongguk.cse.naemansan.service.CourseService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    private final EntityManager em;

    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

    @Bean
    public CourseService courseServiceService() {
        return new CourseService(courseRepository(), courseTypeRepository());
    }
    @Bean
    public CourseRepository courseRepository() {
        return new JpaCourseRepository(em);
    }
    @Bean
    public CourseTypeRepository courseTypeRepository() {
        return new JpaCourseTypeRepository(em);
    }
}
