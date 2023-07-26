package ru.practicum.main.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "locations")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private float lat;
    @NotNull
    private float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(location.lat, lat) == 0
                && Float.compare(location.lon, lon) == 0
                && Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon);
    }
}
