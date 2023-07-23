package ru.practicum.main.request.entity;

import lombok.*;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.states.RequestStatus;
import ru.practicum.main.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id", nullable = false)
    @ToString.Exclude
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    @ToString.Exclude
    private Event event;
    @Column(name = "created_date")
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) && Objects.equals(requester,
                request.requester) && status == request.status && Objects.equals(event, request.event)
                && Objects.equals(created, request.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requester, status, event, created);
    }
}
