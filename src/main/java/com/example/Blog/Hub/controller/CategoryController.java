package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.dto.CategoryDTO;
import com.example.Blog.Hub.entity.Category;
import com.example.Blog.Hub.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryService service;


    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(service.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id){
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategory(){
        return ResponseEntity.ok(service.getAllCategory());
    }

    @PutMapping("/category")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(service.updateCategory(categoryDTO),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        service.deleteCategory(id);
        return ResponseEntity.ok("Deleted Successfully");
    }


}
