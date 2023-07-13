package stat.service.mapper;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.dto.dtos.EndpointHit;
import stat.service.hit.entity.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HitMapperTest {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final HitMapper mapper = new HitMapper();
    private final EasyRandom generator = new EasyRandom();

    @Test
    void testHitMapper_whenCorrectData_thenReturnedCorrectValue() {
        EndpointHit endpointHit = generator.nextObject(EndpointHit.class);
        endpointHit.setTimestamp("2020-05-05 00:00:00");
        Hit hit = mapper.toHit(endpointHit);

        assertEquals(hit.getApp(), endpointHit.getApp());
        assertEquals(hit.getIp(), endpointHit.getIp());
        assertEquals(hit.getUri(), endpointHit.getUri());
        assertEquals(hit.getTimestamp(), LocalDateTime.parse(endpointHit.getTimestamp(), formatter));
    }
}