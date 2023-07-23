package ru.practicum.main.compilation.entity;

import lombok.*;
import ru.practicum.main.event.entity.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    private List<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean pinned;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return Objects.equals(events, that.events)
                && Objects.equals(id, that.id)
                && Objects.equals(pinned, that.pinned)
                && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events, id, pinned, title);
    }
}
