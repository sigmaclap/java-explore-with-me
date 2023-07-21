package ru.practicum.main.compilation;

import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilationPatch(Long compId, UpdateCompilationRequest request);

    List<CompilationDto> getCompilationOfEvents(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
