package ru.practicum.main.comment.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "text")
    String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    User user;
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id)
                && Objects.equals(text, comment.text)
                && Objects.equals(event, comment.event)
                && Objects.equals(user, comment.user)
                && Objects.equals(createdDate, comment.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, event, user, createdDate);
    }
}
