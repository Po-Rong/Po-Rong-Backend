package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.dto.request.ReviewRequestDto;
import com.porong.backend.service.ReviewService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReviewController {
	@Autowired
    private ReviewService reviewService;

    // 팝업별 리뷰 목록 조회 (방문 시간 포함 및 동적 정렬)
    // GET /api/popups/{popupId}/reviews?sort=rating_high
    @GetMapping("/popups/{popupId}/reviews")
    public ResponseEntity<?> getReviewsByPopupId(
            @PathVariable("popupId") Long popupId,
            @RequestParam(value = "sort", required = false) String sort) {
        return reviewService.getReviewsByPopupId(popupId, sort);
    }

    // 리뷰 등록 + 누적 개수별 산리오 키링 증정
    // POST /api/popups/{popupId}/reviews
    @PostMapping("/popups/{popupId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable("popupId") Long popupId,
            @RequestBody ReviewRequestDto requestDto) {
        return reviewService.createReview(popupId, requestDto);
    }

    // 리뷰 수정 (본인 검증 403 처리)
    // PATCH /api/reviews/{reviewId}
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewRequestDto requestDto) {
        return reviewService.updateReview(reviewId, requestDto);
    }
    
    // 리뷰 삭제 (본인 검증 403 처리)
    // DELETE /api/reviews/{reviewId}?user_id=1
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("user_id") Long userId) {
        return reviewService.deleteReview(reviewId, userId);
    }
}
