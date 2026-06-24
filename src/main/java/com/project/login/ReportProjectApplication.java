package com.project.login;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

//@Author Ritesh C
@SpringBootApplication
@ComponentScan(basePackages = "com.project")
@EnableScheduling
@EnableAsync
@EnableCaching
public class ReportProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportProjectApplication.class, args);
    }
}
