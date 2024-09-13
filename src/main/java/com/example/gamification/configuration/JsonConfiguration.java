package com.example.gamification.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;

public class JsonConfiguration {

    @Bean
    public Module hibernateModule() {
        return new Hibernate6Module();
    }

}
