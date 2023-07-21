package stat.service.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.ViewStats;
import stat.service.mapper.HitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitServiceImpl implements HitService {
    private final HitRepository repository;
    private final HitMapper mapper;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        log.info("Saving hit " + endpointHit);
        repository.save(mapper.toHit(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (Boolean.TRUE.equals(unique)) {
                return repository.getUniqueIpViewStatsNoUri(start, end);
            } else {
                return repository.getNotUniqueIpViewStatsNoUri(start, end);
            }
        } else {
            if (Boolean.TRUE.equals(unique)) {
                return repository.getUniqueIpViewStatsWithUris(start, end, uris);
            } else {
                return repository.getNotUniqueIpViewStatsWithUris(start, end, uris);
            }
        }
    }
}
