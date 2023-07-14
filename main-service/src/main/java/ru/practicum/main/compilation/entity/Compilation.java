package ru.practicum.main.compilation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.event.entity.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    private List<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean pinned;
    private String title;
}
