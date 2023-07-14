package ru.practicum.main.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.request.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
