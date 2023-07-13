package stat.service.hit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dto.dtos.ViewStats;
import stat.service.hit.entity.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.dto.dtos.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) DESC")
    List<ViewStats> getUniqueIpViewStatsNoUri(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.dto.dtos.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) DESC")
    List<ViewStats> getNotUniqueIpViewStatsNoUri(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.dto.dtos.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "AND h.uri IN (?3) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) DESC")
    List<ViewStats> getUniqueIpViewStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.dto.dtos.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "AND h.uri IN (?3)  " +
            "group by h.app, h.uri " +
            "order by count(h.ip) DESC")
    List<ViewStats> getNotUniqueIpViewStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

}
