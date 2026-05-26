package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins="*")
public class CategoryController {
	
    @Autowired
    private CategoryService categoryService;

    // 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<?> getCategories() {
        return categoryService.getCategories();
    }
}
