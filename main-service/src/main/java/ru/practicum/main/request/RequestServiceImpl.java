package ru.practicum.main.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.exceptions.EventNotFoundException;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.exceptions.RequestNotFoundException;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.entity.Request;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.states.RequestStatus;
import ru.practicum.main.states.State;
import ru.practicum.main.user.UserRepository;
import ru.practicum.main.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository repository;
    private final RequestMapper mapper;

    @Override
    public List<ParticipationRequestDto> getParticipationInEventsByUserId(Long userId) {
        validateExistsUser(userId);
        return repository.findAllByRequester_Id(userId).stream()
                .map(mapper::toDtoRequest)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequestByUserOnParticipation(Long userId, Long eventId) {
        Request request = new Request();
        validateExistsUser(userId);
        validateExistsEvent(eventId);
        User userRequester = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        List<Request> confirmedRequests = repository.findConfirmedRequest(RequestStatus.CONFIRMED, eventId);
        validateCreateByUserRequest(userId, eventId, userRequester, event, confirmedRequests);
        updateRequestData(request, userRequester, event);
        return mapper.toDtoRequest(repository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        validateExistsUser(userId);
        validateExistsRequest(requestId);
        User userRequester = userRepository.getReferenceById(userId);
        Request request = repository.getReferenceById(requestId);
        validateUserOwnerRequest(userRequester, request.getRequester());
        request.setStatus(RequestStatus.CANCELED);
        return mapper.toDtoRequest(repository.save(request));
    }

    private static void validateUserOwnerRequest(User userOwnerRequest, User userRequester) {
        if (!userOwnerRequest.equals(userRequester)) {
            log.error("Only the user who created the request can cancel it.");
            throw new InvalidDataException("Only the user who created the request can cancel it.");
        }
    }

    private void validateExistsUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.error("User with id not found: " + userId);
            throw new UserNotFoundException("User with id not found: " + userId);
        }
    }

    private void validateExistsEvent(Long eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            log.error("Event with id not found: " + eventId);
            throw new EventNotFoundException("Event with id was not found: " + eventId);
        }
    }

    private void validateExistsRequest(Long requestId) {
        if (repository.findById(requestId).isEmpty()) {
            log.error("Request with id not found: " + requestId);
            throw new RequestNotFoundException("Request with id not found: " + requestId);
        }
    }

    private static void updateRequestData(Request request, User userRequester, Event event) {
        if (event.getRequestModeration().equals(Boolean.FALSE) || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        request.setCreated(LocalDateTime.now());
        request.setRequester(userRequester);
        request.setEvent(event);
    }

    private void validateCreateByUserRequest(Long userId, Long eventId, User userRequester, Event event,
                                             List<Request> confirmedRequests) {
        if (userRequester.equals(event.getInitiator())) {
            log.error("Initiator of the event cannot add a request to participate in his event");
            throw new InvalidDataException("Initiator of the event cannot add a request to participate in his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Can't participate in an unpublished event");
            throw new InvalidDataException("Can't participate in an unpublished event");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == confirmedRequests.size()) {
            log.error("Limit of requests for participation has been reached");
            throw new InvalidDataException("Limit of requests for participation has been reached");
        }
        boolean isCheckRepeatedRequest = repository.checkRepeatedRequest(eventId, userId);
        if (isCheckRepeatedRequest) {
            log.error("Can't add a repeat request");
            throw new InvalidDataException("Can't add a repeat request");
        }
    }
}
