package com.porong.backend.controller;

import com.porong.backend.dto.response.MyPageReviewResponseDto;
import com.porong.backend.mapper.MyPageReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class MyPageReviewController {

    @Autowired
    private MyPageReviewMapper myPageReviewMapper;

    // 마이페이지: 내가 작성한 리뷰 목록 조회 엔드포인트
    @GetMapping("/me")
    public ResponseEntity<List<MyPageReviewResponseDto>> getMyReviews(@RequestParam("user_id") Long userId) {
        List<MyPageReviewResponseDto> myReviews = myPageReviewMapper.selectMyReviews(userId);
        return ResponseEntity.ok(myReviews);
    }
}