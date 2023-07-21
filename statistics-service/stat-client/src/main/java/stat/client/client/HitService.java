package stat.client.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.ViewStats;
import ru.dto.dtos.utils.Constants;
import stat.client.client.mapper.ClientMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class HitService {


    private final StatsClient webClient;
    private final ClientMapper mapper;

    public void saveHit(HttpServletRequest request) {
        EndpointHit hit = mapper.toHit(request);
        webClient
                .webClientWithTimeout()
                .post()
                .uri("/hit")
                .body(Mono.just(hit), EndpointHit.class)
                .retrieve()
                .bodyToMono(EndpointHit.class)
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
                        .queryParam("start", (start).format(Constants.formatter))
                        .queryParam("end", (end).format(Constants.formatter))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntityList(ViewStats.class)
                .block();
    }

    public Long getViews(List<Long> eventsIds) {
        List<String> uris = new ArrayList<>();
        Long currentViews = 0L;
        for (Long eventId : eventsIds) {
            uris.add("/events/" + eventId);
        }
        LocalDateTime start = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0);
        List<ViewStats> stats = getStats(start, end, uris, true).getBody();
        if (stats != null) {
            for (ViewStats stat : stats) {
                currentViews += stat.getHits();
            }
        } else {
            return currentViews;
        }
        return currentViews;
    }
}
