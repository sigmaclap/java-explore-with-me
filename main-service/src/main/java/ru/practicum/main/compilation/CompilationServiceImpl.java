package ru.practicum.main.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.entity.Compilation;
import ru.practicum.main.compilation.mapper.CompilationMapper;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.exceptions.CompilationNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.utils.CommonPatterns.patternPageable;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = mapper.toCompilation(compilationDto);
        getCompilationsOfEvents(compilationDto, compilation);
        return mapper.toDto(repository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        repository.delete(repository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation not found and not deleted")));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationPatch(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation not found"));
        updateCompilationEntity(request, compilation);
        return mapper.toDto(repository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilationOfEvents(boolean pinned, Integer from, Integer size) {
        if (pinned) {
            return repository.findAllByPinnedTrue(patternPageable(from, size)).stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByPinnedFalse(patternPageable(from, size)).stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return mapper.toDto(repository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation not found")));
    }

    private void getCompilationsOfEvents(NewCompilationDto compilationDto, Compilation compilation) {
        List<Event> eventsToCreate = updateListEvents(compilationDto.getEvents());
        compilation.setEvents(eventsToCreate);
    }

    private List<Event> updateListEvents(List<Long> eventsIds) {
        List<Event> eventsToCreate = new ArrayList<>();
        if (eventsIds != null) {
            for (Long ids : eventsIds) {
                eventsToCreate.add(eventRepository.getReferenceById(ids));
            }
        }
        return eventsToCreate;
    }

    private void updateCompilationEntity(UpdateCompilationRequest request, Compilation compilation) {
        List<Event> eventsToCreate = updateListEvents(request.getEvents());
        compilation.setEvents(eventsToCreate);
        if (request.getPinned() != null) compilation.setPinned(request.getPinned());
        if (request.getTitle() != null) compilation.setTitle(request.getTitle());
    }
}
