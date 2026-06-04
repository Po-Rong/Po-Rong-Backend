package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.service.RegionService;

@RestController
@RequestMapping("/api/regions")
@CrossOrigin(origins="*")
public class RegionController {
	
    @Autowired
    private RegionService regionService;

    // 지역 전체 조회
    @GetMapping
    public ResponseEntity<?> getRegions() {
        return regionService.getRegions();
    }
}
