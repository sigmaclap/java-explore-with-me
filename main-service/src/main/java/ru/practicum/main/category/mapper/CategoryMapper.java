package ru.practicum.main.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.entity.Category;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto toDtoCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
