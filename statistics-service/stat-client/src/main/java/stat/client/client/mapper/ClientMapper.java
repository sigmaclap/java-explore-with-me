package stat.client.client.mapper;

import lombok.experimental.UtilityClass;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@UtilityClass
public class ClientMapper {

    public EndpointHit toHit(HttpServletRequest request) {
        return EndpointHit.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("main-service")
                .timestamp(LocalDateTime.now().format(Constants.formatter))
                .build();
    }
}
