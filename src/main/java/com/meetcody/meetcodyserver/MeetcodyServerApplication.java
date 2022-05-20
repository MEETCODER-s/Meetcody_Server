package com.meetcody.meetcodyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MeetcodyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetcodyServerApplication.class, args);
    }

}
