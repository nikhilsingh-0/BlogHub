package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.CategoryDTO;
import com.example.Blog.Hub.entity.Category;
import com.example.Blog.Hub.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository repository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category =  repository.save(getCategory(categoryDTO));
        return getCategoryDTO(category);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> optional =  repository.findById(id);
        if (optional.isEmpty()){
            return null;
        }
        return getCategoryDTO(optional.get());
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Optional<Category> optional =  repository.findById(categoryDTO.getId());
        if (optional.isEmpty()){
            return null;
        }
        Category category = optional.get();
        category.setCategoryTittle(categoryDTO.getCategoryTittle());
        category.setCategoryDescription(categoryDTO.getCategoryTittle());

        Category updatedCategory = repository.save(category);

        return getCategoryDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (repository.findById(id).isEmpty()){
            return;
        }
         repository.deleteById(id);
    }

    @Override
    public List<Category> getAllCategory() {
        return repository.findAll();
    }

    private Category getCategory(CategoryDTO categoryDTO){
        return modelMapper.map(categoryDTO,Category.class);
    }

    private CategoryDTO getCategoryDTO(Category category){
        return modelMapper.map(category,CategoryDTO.class);
    }
}
