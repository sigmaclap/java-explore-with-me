package ru.practicum.main.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(Long catId) {

    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        return null;
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return null;
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return null;
    }
}
