package stat.service.hit;


import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void saveHit(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
