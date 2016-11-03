package com.bitreight.tasklist.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.bitreight.tasklist.dao" })
public class DaoContextConfiguration {
}
