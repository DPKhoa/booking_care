package com.app.booking_care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookingCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingCareApplication.class, args);
    }

}
