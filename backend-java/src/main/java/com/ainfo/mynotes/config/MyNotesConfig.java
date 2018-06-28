package com.ainfo.mynotes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.ainfo.mynotes")
public class MyNotesConfig {
}
