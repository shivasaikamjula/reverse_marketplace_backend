package com.marketplace.service;

import com.marketplace.dto.CategoryRequest;
import com.marketplace.model.ServiceCategory;
import com.marketplace.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ServiceCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    public ServiceCategory createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category with this name already exists!");
        }

        ServiceCategory category = new ServiceCategory(
                request.getName(),
                request.getDescription(),
                request.getIcon() != null ? request.getIcon() : "Layers"
        );
        return categoryRepository.save(category);
    }

    public ServiceCategory updateCategory(Long id, CategoryRequest request) {
        ServiceCategory category = getCategoryById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
