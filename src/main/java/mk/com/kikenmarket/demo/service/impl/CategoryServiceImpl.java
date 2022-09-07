package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.exceptions.CategoryNotFoundException;
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
    public void saveCategory(String name, String description, List<String> keyWords, Long categoryID) {
        if (categoryID == null){
            categoryRepository.save(new Category(name, description, keyWords));
        } else {
            Category category = this.categoryRepository.findByCategoryID(categoryID)
                    .orElseThrow(()-> new CategoryNotFoundException(categoryID));
            category.setName(name);
            category.setDescription(description);
            category.setKeyWords(keyWords);
            this.categoryRepository.save(category);
        }
    }

    @Override
    public List<Category> listAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findByID(Long id) {
        return categoryRepository.findByCategoryID(id)
                .orElseThrow(()-> new CategoryNotFoundException(id));
    }

    @Override
    public void deleteCategory(Long id) {
        this.categoryRepository.deleteById(id);
    }
}
