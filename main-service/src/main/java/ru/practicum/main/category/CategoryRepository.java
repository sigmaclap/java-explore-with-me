package ru.practicum.main.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
