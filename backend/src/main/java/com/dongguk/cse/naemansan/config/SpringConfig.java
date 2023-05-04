package com.dongguk.cse.naemansan.config;

import com.dongguk.cse.naemansan.repository.JpaWalkwayRepository;
import com.dongguk.cse.naemansan.repository.Walkwayrepository;
import com.dongguk.cse.naemansan.service.WalkwayService;
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
    public WalkwayService walkwayService(){
        return new WalkwayService(walkwayrepository());
    }
    @Bean
    public Walkwayrepository walkwayrepository(){
        return new JpaWalkwayRepository(em);
    }
}
