package stat.service.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dto.dtos.EndpointHit;
import ru.dto.dtos.ViewStats;
import ru.dto.dtos.utils.Constants;
import stat.service.exceptions.InvalidValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class HitController {

    private final HitService hitService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHit endpointHit) {
        hitService.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = Constants.DATE_FORMAT) @NotNull LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = Constants.DATE_FORMAT) @NotNull LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidValidationException("Start time must be before end time!");
        }
        return hitService.getStats(start, end, uris, unique);
    }
}
