package ru.practicum.main.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.entity.Category;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.utils.CommonPatterns.patternPageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Creating new category {}", categoryDto.getName());
        return mapper.toDtoCategory(repository.save(mapper.toCategory(categoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Deleting category with id {}", catId);
        repository.delete(repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found and not deleted")));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id"));
        category.setName(categoryDto.getName());
        return mapper.toDtoCategory(repository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return repository.findAll(patternPageable(from, size)).stream()
                .map(mapper::toDtoCategory)
                .collect(Collectors.toList());
    }


    @Override
    public CategoryDto getCategoryById(Long catId) {
        return mapper.toDtoCategory(repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id")));
    }
}
