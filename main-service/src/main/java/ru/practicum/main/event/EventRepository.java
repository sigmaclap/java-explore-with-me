package ru.practicum.main.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
