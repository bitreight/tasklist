package com.bitreight.tasklist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(JpaConfiguration.class)
@ImportResource("classpath:/config/jpaConfiguration.xml")
public class BackendConfiguration {
}
