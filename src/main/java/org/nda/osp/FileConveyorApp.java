package org.nda.osp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
public class FileConveyorApp {

    public static void main(String... args) {
        SpringApplication.run(FileConveyorApp.class, args);
    }
}
