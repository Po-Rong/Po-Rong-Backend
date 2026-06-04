package com.porong.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.porong.backend.dto.response.CollectionResponseDto;
import com.porong.backend.mapper.CollectionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionMapper collectionMapper;

    public List<CollectionResponseDto> getMyCollections(Long userId) {
        return collectionMapper.selectMyCollections(userId);
    }
}