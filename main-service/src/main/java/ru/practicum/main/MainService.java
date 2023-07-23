package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import stat.client.client.HitService;
import stat.client.client.StatsClient;

@SpringBootApplication
@Import({HitService.class, StatsClient.class})
public class MainService {
    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}
