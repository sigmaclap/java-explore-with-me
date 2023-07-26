package ru.practicum.main.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.CompilationService;
import ru.practicum.main.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationsControllerPublic {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilationOfEvents(@RequestParam(required = false) boolean pinned,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get compilation with states pinned {}, from {}, size {}", pinned, from, size);
        return compilationService.getCompilationOfEvents(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Get compilation with id {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
