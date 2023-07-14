package ru.practicum.main.compilation;

import org.springframework.stereotype.Service;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;

import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        return null;
    }

    @Override
    public void deleteCompilation(Long compId) {

    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        return null;
    }

    @Override
    public List<CompilationDto> getCompilationOfEvents(Boolean pinned, Integer from, Integer size) {
        return null;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return null;
    }
}
