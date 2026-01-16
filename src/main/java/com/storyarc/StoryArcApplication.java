package com.storyarc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.storyarc.mapper")
public class StoryArcApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryArcApplication.class, args);
    }

}
