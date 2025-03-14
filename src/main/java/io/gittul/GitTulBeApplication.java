package io.gittul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GitTulBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitTulBeApplication.class, args);
    }
}
