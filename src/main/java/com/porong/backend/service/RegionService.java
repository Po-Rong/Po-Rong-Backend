package com.porong.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.porong.backend.mapper.RegionMapper;
import com.porong.backend.vo.RegionVO;

@Service
public class RegionService {
	
    @Autowired
    private RegionMapper regionMapper;

    public ResponseEntity<?> getRegions() {
        List<RegionVO> regions = regionMapper.findAll();
        return ResponseEntity.ok(regions);
    }
}
