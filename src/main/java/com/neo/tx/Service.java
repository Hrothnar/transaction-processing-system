package com.neo.tx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class  Service {

    static void main(String[] args) {
        SpringApplication.run(Service.class, args);
    }
}