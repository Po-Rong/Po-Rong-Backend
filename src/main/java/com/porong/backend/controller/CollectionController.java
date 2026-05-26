package com.porong.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.dto.response.CollectionResponseDto;
import com.porong.backend.service.CollectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collections") // 명세서 Prefix 경로 반영
public class CollectionController {

    private final CollectionService collectionService;

    // 나의 도감 키링 리스트 조회
    // 실제 주소 규칙: GET /api/collections/me?user_id=1
    @GetMapping("/me")
    public ResponseEntity<List<CollectionResponseDto>> getMyCollections(@RequestParam("user_id") Long userId) {
        List<CollectionResponseDto> list = collectionService.getMyCollections(userId);
        return ResponseEntity.ok(list);
    }
}