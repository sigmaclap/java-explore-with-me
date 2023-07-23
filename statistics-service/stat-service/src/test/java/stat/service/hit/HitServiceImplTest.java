package stat.service.hit;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.ViewStats;
import stat.service.hit.entity.Hit;
import stat.service.mapper.HitMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HitServiceImplTest {
    private HitService service;
    @Mock
    private HitRepository repository;

    private final EasyRandom generator = new EasyRandom();

    @BeforeEach
    void setUp() {
        service = new HitServiceImpl(repository);
    }

    @Test
    void saveHit_whenValidData_thenSavedHit() {
        EndpointHit endpointHit = generator.nextObject(EndpointHit.class);
        endpointHit.setTimestamp("2020-05-05 00:00:00");
        Hit hitTest = HitMapper.toHit(endpointHit);

        service.saveHit(endpointHit);

        verify(repository, times(1)).save((hitTest));
    }

    @Test
    void getStats_whenUniqueTrueAndUrisNull_thenGetStatsReturned() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<String> uris = Collections.emptyList();
        Boolean unique = true;
        List<ViewStats> expectedList = List.of(generator.nextObject(ViewStats.class));
        when(repository.getUniqueIpViewStatsNoUri(start, end))
                .thenReturn(expectedList);

        List<ViewStats> actualStats = service.getStats(start, end, uris, unique);

        assertEquals(1, actualStats.size());
        assertEquals(expectedList, actualStats);
    }

    @Test
    void getStats_whenUniqueFalseAndUrisNull_thenGetStatsReturned() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<String> uris = Collections.emptyList();
        Boolean unique = false;
        List<ViewStats> expectedList = List.of(generator.nextObject(ViewStats.class));
        when(repository.getNotUniqueIpViewStatsNoUri(start, end))
                .thenReturn(expectedList);

        List<ViewStats> actualStats = service.getStats(start, end, uris, unique);

        assertEquals(1, actualStats.size());
        assertEquals(expectedList, actualStats);
    }

    @Test
    void getStats_whenUniqueTrueAndUrisNotNull_thenGetStatsReturned() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<String> uris = List.of("123");
        Boolean unique = true;
        List<ViewStats> expectedList = List.of(generator.nextObject(ViewStats.class));
        when(repository.getUniqueIpViewStatsWithUris(start, end, uris))
                .thenReturn(expectedList);

        List<ViewStats> actualStats = service.getStats(start, end, uris, unique);

        assertEquals(1, actualStats.size());
        assertEquals(expectedList, actualStats);
    }

    @Test
    void getStats_whenUniqueFalseAndUrisNotNull_thenGetStatsReturned() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<String> uris = List.of("123");
        Boolean unique = false;
        List<ViewStats> expectedList = List.of(generator.nextObject(ViewStats.class));
        when(repository.getNotUniqueIpViewStatsWithUris(start, end, uris))
                .thenReturn(expectedList);

        List<ViewStats> actualStats = service.getStats(start, end, uris, unique);

        assertEquals(1, actualStats.size());
        assertEquals(expectedList, actualStats);
    }
}