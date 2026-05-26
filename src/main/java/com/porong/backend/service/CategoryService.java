package com.porong.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.porong.backend.mapper.CategoryMapper;
import com.porong.backend.vo.CategoryVO;

@Service
public class CategoryService {
	
    @Autowired
    private CategoryMapper categoryMapper;

    public ResponseEntity<?> getCategories() {
        List<CategoryVO> categories = categoryMapper.findAll();
        return ResponseEntity.ok(categories);
    }
}
