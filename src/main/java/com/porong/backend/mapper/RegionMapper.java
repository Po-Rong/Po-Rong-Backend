package com.porong.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.porong.backend.vo.RegionVO;

@Mapper
public interface RegionMapper {
	
    // 지역 전체 조회
    @Select("SELECT * FROM regions")
    List<RegionVO> findAll();
}
