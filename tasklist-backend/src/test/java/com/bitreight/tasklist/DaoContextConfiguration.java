package com.bitreight.tasklist;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.bitreight.tasklist.dao" })
class DaoContextConfiguration {
}
