package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import stat.client.client.HitService;
import stat.client.client.StatsClient;
import stat.client.client.mapper.ClientMapper;

@SpringBootApplication
@Import({HitService.class, ClientMapper.class, StatsClient.class})
public class MainService {
    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}
