package com.neo.tx.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() { // called upon @CreatedBy and @LastModifiedBy
        // a user's name could be retrieved through Spring Security
        // return () -> Optional.of(SecurityContextHolder.getContext().getAuthentication().getName()); // TODO uncomment in case of including String Security
        return () -> Optional.of("system");
    }
}