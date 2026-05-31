package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    // 최신 리뷰 3건 리스트
    @GetMapping("/reviews/recent")
    public ResponseEntity<?> getRecentThreeReviews() {
        return reviewService.getRecentThreeReviews();
    }

    // 리뷰 등록 + 누적 개수별 산리오 키링 증정
    // POST /api/popups/{popupId}/reviews
    @PostMapping(value = "/popups/{popupId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(
            @PathVariable("popupId") Long popupId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("rating") int rating,
            @RequestParam("congestionLevel") String congestionLevel,
            @RequestParam("reservationId") Long reservationId,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setUserId(userId);
        requestDto.setContent(content);
        requestDto.setRating(rating);
        requestDto.setCongestionLevel(congestionLevel);
        requestDto.setReservationId(reservationId);

        return reviewService.createReview(popupId, requestDto, image);
    }

    // 리뷰 수정 (본인 검증 403 처리)
    // PATCH /api/reviews/{reviewId}
    @PatchMapping(value = "/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("rating") int rating,
            @RequestParam("congestionLevel") String congestionLevel,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setUserId(userId);
        requestDto.setContent(content);
        requestDto.setRating(rating);
        requestDto.setCongestionLevel(congestionLevel);

        return reviewService.updateReview(reviewId, requestDto, image);
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
