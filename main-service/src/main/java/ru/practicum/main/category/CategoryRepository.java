package ru.practicum.main.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
