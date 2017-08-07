package ru.ifmo.ctddev.isaev.studentsdb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.net.URI;


/**
 * @author iisaev
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            }
        };
    }

}