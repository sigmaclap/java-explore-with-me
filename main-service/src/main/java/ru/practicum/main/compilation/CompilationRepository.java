package ru.practicum.main.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.compilation.entity.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinnedTrue(Pageable pageable);

    Page<Compilation> findAllByPinnedFalse(Pageable pageable);

}
