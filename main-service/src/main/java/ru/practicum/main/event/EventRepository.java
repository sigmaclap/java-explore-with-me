package ru.practicum.main.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.states.State;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Page<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Event findByIdAndInitiator_Id(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long eventId, State state);
}
