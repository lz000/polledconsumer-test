package com.sample;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, "--spring.cloud.stream.pollable-source=myDestination");
    }
    @Bean
    public ApplicationRunner poller(PollableMessageSource destIn) {
        return args -> {
            while (true) {
                try {
                    if (!destIn.poll(m -> {
                        String newPayload = (new String((byte[]) m.getPayload())).toUpperCase();
                        System.out.println("PAYLOAD: " + newPayload);
                    })) {
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}