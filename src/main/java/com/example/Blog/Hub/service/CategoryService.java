package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.CategoryDTO;
import com.example.Blog.Hub.entity.Category;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    List<Category> getAllCategory();

}
