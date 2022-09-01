package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.repository.CategoryRepository;
import mk.com.kikenmarket.demo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> addCategory(String name, String description, List<String> keyWords) {
        return Optional.of(categoryRepository.save(new Category(name, description, keyWords)));
    }

    @Override
    public List<Category> listAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findByID(Long id) {
        return categoryRepository.findByCategoryID(id);
    }
}
