package ru.practicum.main.event;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.CategoryRepository;
import ru.practicum.main.category.entity.Category;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.exceptions.BadRequestException;
import ru.practicum.main.exceptions.EventNotFoundException;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.request.RequestRepository;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.entity.Request;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.states.EventSortType;
import ru.practicum.main.states.RequestStatus;
import ru.practicum.main.states.State;
import ru.practicum.main.user.UserRepository;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.utils.UpdaterDtoToEntity;
import stat.client.client.HitService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.entity.QEvent.event;
import static ru.practicum.main.utils.CommonPatterns.patternPageable;
import static ru.practicum.main.utils.CommonPatterns.patternPageableWithSort;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;
    private final HitService hitService;
    private final UpdaterDtoToEntity update;

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<State> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        Integer from, Integer size) {
        Page<Event> result;
        BooleanBuilder bb = new BooleanBuilder();
        if (users != null) {
            bb.and(event.initiator.id.in(users));
        }
        if (states != null) {
            bb.and(event.state.in(states));
        }
        if (categories != null) {
            bb.and(event.category.id.in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            bb.and(event.eventDate.between(rangeStart, rangeEnd));
        }
        if (bb.getValue() != null) {
            result = repository.findAll(bb.getValue(), patternPageable(from, size));
        } else {
            result = repository.findAll(patternPageable(from, size));
        }
        updateViews(result.getContent());
        return result.stream()
                .map(mapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto patchEventByAdmin(Long eventId, UpdateEventRequest request) {
        validateExistsEvent(eventId);
        Event event = repository.getReferenceById(eventId);
        validatedTimeToStartEventByAdmin(request.getEventDate());
        validatePendingStatusForUpdate(event);
        update.fromDtoToEntity(request, event);
        updateViews(List.of(event));
        return mapper.toFullDto(repository.save(event));
    }


    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        validateExistsUser(userId);
        List<Event> events = repository.findAllByInitiator_Id(userId, patternPageable(from, size)).getContent();
        updateViews(events);
        return events.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto createEventByUserId(Long userId, NewEventDto newEventDto) {
        validateExistsUser(userId);
        validatedTimeToStartEvent(newEventDto.getEventDate());
        User initiator = userRepository.getReferenceById(userId);
        Category eventCategory = categoryRepository.getReferenceById(newEventDto.getCategory());
        Event event = mapper.toEvent(newEventDto);
        setInitialParameters(initiator, eventCategory, event);
        updateViews(List.of(event));
        return mapper.toFullDto(repository.save(event));
    }


    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        validateExistsUser(userId);
        validateExistsEvent(eventId);
        Event event = repository.findByIdAndInitiator_Id(eventId, userId);
        updateViews(List.of(event));
        return mapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventWithUserIdAndEventId(Long userId, Long eventId, UpdateEventRequest request) {
        validateExistsUser(userId);
        validateExistsEvent(eventId);
        validatedTimeToStartEvent(request.getEventDate());
        Event event = repository.getReferenceById(eventId);
        validateEventStatusForUpdate(event);
        update.fromDtoToEntity(request, event);
        updateViews(List.of(event));
        return mapper.toFullDto(repository.save(event));
    }


    @Override
    public List<ParticipationRequestDto> getEventByParticipationByUser(Long userId, Long eventId) {
        validateExistsUser(userId);
        validateExistsEvent(eventId);
        Event event = repository.getReferenceById(eventId);
        isCheckUserInitiatorEvent(userId, event.getInitiator());
        return requestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toDtoRequest)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseStatusUpdateResult updateEventParticipationUsers(Long userId, Long eventId,
                                                                         EventRequestStatusUpdateResult request) {
        validateExistsUser(userId);
        validateExistsEvent(eventId);
        Event event = repository.getReferenceById(eventId);
        isCheckUserInitiatorEvent(userId, event.getInitiator());
        List<Request> requests = getListWithStatusPendingAndUserInitiator(userId, request);
        List<Request> confirmedRequest = getConfirmedRequest(eventId);
        validateRequestsNotEmpty(requests);
        if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(Boolean.FALSE)) {
            requests.forEach(req -> req.setStatus(RequestStatus.CONFIRMED));
            List<ParticipationRequestDto> requestsDto = requests.stream()
                    .map(requestMapper::toDtoRequest)
                    .collect(Collectors.toList());
            return mapper.toDtoResponseStatus(requestsDto);
        }
        validateParticipantLimit(event, confirmedRequest);
        updateRequestsStatus(request, event, requests, confirmedRequest);
        saveRequests(event, requests);
        return mapper.toDtoResponseStatus(requests.stream()
                .map(requestMapper::toDtoRequest)
                .collect(Collectors.toList()));
    }

    @Override
    public List<EventShortDto> getEventsWithSortByText(String text,
                                                       List<Long> categories,
                                                       Boolean paid, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd, boolean onlyAvailable,
                                                       EventSortType sort, Integer from, Integer size,
                                                       HttpServletRequest request) {
        Page<Event> resultEvents;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (text != null) {
            booleanBuilder.and(event.annotation.containsIgnoreCase(text))
                    .or(event.description.containsIgnoreCase(text));
        }
        if (categories != null) {
            booleanBuilder.and(event.category.id.in(categories));
        }
        if (paid != null) {
            booleanBuilder.and(event.paid.eq(paid));
        }
        if (rangeStart != null && rangeEnd != null) {
            booleanBuilder.and(event.eventDate.between(rangeStart, rangeEnd));
        }
        if (onlyAvailable) {
            booleanBuilder.and(event.participantLimit.eq(0L))
                    .or(event.participantLimit.gt(event.confirmedRequests));
        }
        if (booleanBuilder.getValue() != null) {
            resultEvents = repository.findAll(booleanBuilder.getValue(), patternPageableWithSort(from, size, sort));
        } else {
            resultEvents = repository.findAll(patternPageableWithSort(from, size, sort));
        }
        updateViews(resultEvents.getContent());
        hitService.saveHit(request);
        return resultEvents.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = repository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException("Event with id was not found"));
        updateViews(List.of(event));
        hitService.saveHit(request);
        return mapper.toFullDto(event);
    }

    private static void validateEventStatusForUpdate(Event event) {
        if (event.getState() != null && (event.getState().equals(State.PUBLISHED)
                || event.getState().equals(State.REJECTED))) {
            log.error("Only pending or canceled events can be changed");
            throw new InvalidDataException("Only pending or canceled events can be changed");
        }
    }

    private void isCheckUserInitiatorEvent(Long userId, User checkUser) {
        User user = userRepository.getReferenceById(userId);
        if (!user.equals(checkUser)) {
            log.info("User if not initiator this event");
            throw new InvalidDataException("User if not initiator this event");
        }
    }

    private void validateExistsUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.error("User with id not found: " + userId);
            throw new UserNotFoundException("User with id not found: " + userId);
        }
    }

    private void validateExistsEvent(Long eventId) {
        if (repository.findById(eventId).isEmpty()) {
            log.error("Event with id not found: " + eventId);
            throw new EventNotFoundException("Event with id was not found: " + eventId);
        }
    }

    private void validatedTimeToStartEvent(LocalDateTime eventTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (eventTime != null && eventTime.isBefore(currentTime.plusHours(2))) {
            log.info("Event date {} is before current time {}", eventTime, currentTime);
            throw new BadRequestException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. Value: " + eventTime);
        }
    }

    private static void validatePendingStatusForUpdate(Event event) {
        if (!event.getState().equals(State.PENDING)) {
            log.error("Cannot publish the event because it's not in the right state: PUBLISHED");
            throw new InvalidDataException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
    }

    private static void setInitialParameters(User initiator, Category eventCategory, Event event) {
        event.setConfirmedRequests(0L);
        event.setViews(0L);
        event.setInitiator(initiator);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(eventCategory);
    }

    private void validatedTimeToStartEventByAdmin(LocalDateTime eventTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (eventTime != null && eventTime.isBefore(currentTime.plusHours(1))) {
            log.info("Event date {} is before current time {}", eventTime, currentTime);
            throw new BadRequestException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. Value: " + eventTime);
        }
    }

    private void updateViews(List<Event> resultEvents) {
        List<Long> eventsIds = resultEvents.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        resultEvents.forEach(v -> v.setViews(hitService.getViews(eventsIds)));
    }

    private void saveRequests(Event event, List<Request> requests) {
        for (Request req : requests) {
            if (req.getStatus().equals(RequestStatus.CONFIRMED)) {
                event.setConfirmedRequests(+1L);
            }
            requestRepository.save(req);
        }
    }

    private static void updateRequestsStatus(EventRequestStatusUpdateResult request, Event event, List<Request> requests, List<Request> confirmedRequest) {
        switch (request.getStatus()) {
            case REJECTED:
                requests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
                break;
            case CONFIRMED:
                long limit = event.getParticipantLimit() - confirmedRequest.size();
                if (requests.size() <= limit) {
                    requests.forEach(req -> req.setStatus(RequestStatus.CONFIRMED));
                } else if (limit > 0) {
                    requests.subList(0, (int) (limit - 1))
                            .forEach((req -> req.setStatus(RequestStatus.CONFIRMED)));
                    requests.subList((int) limit, requests.size())
                            .forEach(req -> req.setStatus(RequestStatus.REJECTED));
                } else {
                    requests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
                }
                break;
        }
    }

    private static void validateParticipantLimit(Event event, List<Request> confirmedRequest) {
        if (event.getParticipantLimit() - confirmedRequest.size() < 1) {
            log.error("The participant limit has been reached");
            throw new InvalidDataException("The participant limit has been reached");
        }
    }

    private List<Request> getConfirmedRequest(Long eventId) {
        return requestRepository.findConfirmedRequest(RequestStatus.CONFIRMED, eventId);
    }

    private List<Request> getListWithStatusPendingAndUserInitiator(Long userId, EventRequestStatusUpdateResult request) {
        return requestRepository.findAllByIdIn(request.getRequestIds()).stream()
                .filter(status -> status.getStatus().equals(RequestStatus.PENDING))
                .filter(user -> user.getEvent().getInitiator().equals(userRepository.getReferenceById(userId)))
                .collect(Collectors.toList());
    }

    private static void validateRequestsNotEmpty(List<Request> requests) {
        if (requests.isEmpty()) {
            log.error("It is not possible to cancel an already confirmed application");
            throw new InvalidDataException("It is not possible to cancel an already confirmed application");
        }
    }
}
