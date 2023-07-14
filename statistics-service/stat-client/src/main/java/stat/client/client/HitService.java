package stat.client.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class HitService {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final StatsClient webClient = new StatsClient();

    public ResponseEntity<EndpointHit> saveHit(EndpointHit endpointHit) {
        return webClient
                .webClientWithTimeout()
                .post()
                .uri("/hit")
                .body(Mono.just(endpointHit), EndpointHit.class)
                .retrieve()
                .toEntity(EndpointHit.class)
                .block();
    }

    public ResponseEntity<List<ViewStats>>
    getStats(LocalDateTime start,
             LocalDateTime end,
             List<String> uris,
             Boolean unique) {
        return webClient
                .webClientWithTimeout()
                .get()
                .uri(builder -> builder.path("/stats")
                        .queryParam("start", encodeTime(start))
                        .queryParam("end", encodeTime(end))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntityList(ViewStats.class)
                .block();
    }

    private static String encodeTime(LocalDateTime value) {
        String timeWithFormat = value.format(formatter);
        return URLEncoder.encode(timeWithFormat, StandardCharsets.UTF_8);
    }
}
