package com.porong.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.porong.backend.vo.CategoryVO;

@Mapper
public interface CategoryMapper {
	
    // 카테고리 전체 조회
    @Select("SELECT * FROM categories ORDER BY id ASC")
    List<CategoryVO> findAll();
}
