package ru.itis.fpvhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FpvHubApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FpvHubApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FpvHubApplication.class);
    }
}
