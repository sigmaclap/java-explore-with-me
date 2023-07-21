package stat.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.utils.Constants;
import stat.service.hit.entity.Hit;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HitMapper {

    public Hit toHit(EndpointHit endpointHit) {
        return Hit.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(), Constants.formatter))
                .build();
    }
}
