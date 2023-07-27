package ru.practicum.main.event.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.category.entity.Category;
import ru.practicum.main.comment.entity.Comment;
import ru.practicum.main.states.State;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.utils.Location;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    String title;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    Category category;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id", nullable = false)
    @ToString.Exclude
    User initiator;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    @ToString.Exclude
    Location location;
    Boolean paid;
    @Column(name = "confirmed_requests")
    Long confirmedRequests;
    @Column(name = "participant_limit")
    Long participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    Long views;
    @Transient
    List<Comment> comments;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(annotation, event.annotation)
                && Objects.equals(title, event.title) && Objects.equals(category, event.category)
                && Objects.equals(createdOn, event.createdOn) && Objects.equals(description, event.description)
                && Objects.equals(eventDate, event.eventDate) && Objects.equals(initiator, event.initiator)
                && Objects.equals(location, event.location) && Objects.equals(paid, event.paid)
                && Objects.equals(confirmedRequests, event.confirmedRequests)
                && Objects.equals(participantLimit, event.participantLimit)
                && Objects.equals(publishedOn, event.publishedOn)
                && Objects.equals(requestModeration, event.requestModeration)
                && state == event.state && Objects.equals(views, event.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, title, category,
                createdOn, description, eventDate, initiator,
                location, paid, confirmedRequests, participantLimit,
                publishedOn, requestModeration, state, views);
    }
}
