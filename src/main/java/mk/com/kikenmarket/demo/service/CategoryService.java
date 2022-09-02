package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void saveCategory(String name, String description, List<String> keyWords, Long categoryID);
    List<Category> listAllCategories();
    Category findByID(Long id);
    void deleteCategory(Long id);
}
