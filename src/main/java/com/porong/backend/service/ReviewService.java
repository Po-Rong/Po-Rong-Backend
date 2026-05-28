package com.porong.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porong.backend.dto.request.ReviewRequestDto;
import com.porong.backend.dto.response.AdminReviewResponseDto;
import com.porong.backend.dto.response.ReviewCreateResponseDto;
import com.porong.backend.dto.response.ReviewListResponseDto;
import com.porong.backend.mapper.ReservationMapper;
import com.porong.backend.mapper.ReviewMapper;
import com.porong.backend.vo.ReviewVO;

@Service
public class ReviewService {
	
	@Autowired
    private ReviewMapper reviewMapper;
	
    @Autowired
    private ReservationMapper reservationMapper;

    // 팝업별 리뷰 목록 조회 (예약 방문 시간 포함)
    @Transactional(readOnly = true)
    public ResponseEntity<?> getReviewsByPopupId(Long popupId, String sort) {
        if (sort == null || sort.isEmpty()) {
            sort = "latest";
        }
        List<ReviewListResponseDto> reviews = reviewMapper.selectReviewsByPopupId(popupId, sort);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 등록 + 누적 횟수별 캐릭터 키링 보상 지급
    @Transactional
    public ResponseEntity<?> createReview(Long popupId, ReviewRequestDto dto) {
        
        // 이 유저가 여태까지 썼던 기존 리뷰 개수 조회
        int currentReviewCount = reviewMapper.countReviewsByUserId(dto.getUserId());
        
        // 이번 리뷰가 성공하면 도달하게 될 누적 리뷰 차례 계산
        int nextReviewCount = currentReviewCount + 1;

        // 원래 하던 대로 리뷰 인서트 실행
        ReviewVO review = new ReviewVO();
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setCongestionLevel(dto.getCongestionLevel());
        review.setReviewImageUrl(dto.getReviewImageUrl());
        review.setPopupId(popupId);
        review.setUserId(dto.getUserId());
        review.setReservationId(dto.getReservationId());

        reviewMapper.insertReview(review); // @Options에 의해 review.getId() 채워짐

        // 조건(nextReviewCount)에 매칭되는 키링 족보가 있는지 DB 조회
        Map<String, Object> keyringMap = reviewMapper.findKeyringByReviewCount(nextReviewCount);
        
        Long earnedKeyringId = null;
        String keyringName = "";
        String responseMessage = nextReviewCount + "번째 리뷰가 성공적으로 등록되었습니다!";

        // 1~5번째 리뷰 조건에 걸려 매칭되는 캐릭터 키링이 존재한다면
        if (keyringMap != null) {
            earnedKeyringId = ((Number) keyringMap.get("id")).longValue();
            keyringName = (String) keyringMap.get("name");

            // 리뷰 수에 맞게 인서트
            reviewMapper.insertCollectionBook(dto.getUserId(), earnedKeyringId, popupId);
            
            responseMessage = nextReviewCount + "번째 리뷰가 등록되었으며, '" + keyringName + "' 키링이 도감에 지급되었습니다!";
        }

        // 최종 성공 응답 객체 조립 후 리턴
        ReviewCreateResponseDto response = ReviewCreateResponseDto.builder()
                .success(true)
                .message(responseMessage)
                .reviewId(review.getId())
                .earnedKeyringId(earnedKeyringId)
                .build();

        return ResponseEntity.ok(response);
    }

    // 리뷰 수정
    @Transactional
    public ResponseEntity<?> updateReview(Long reviewId, ReviewRequestDto dto) {
        ReviewVO existingReview = reviewMapper.findById(reviewId);
        if (existingReview == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", "존재하지 않는 리뷰입니다."));
        }

        if (!existingReview.getUserId().equals(dto.getUserId())) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "본인이 작성한 후기만 수정할 수 있습니다."));
        }

        existingReview.setContent(dto.getContent());
        existingReview.setRating(dto.getRating());
        existingReview.setCongestionLevel(dto.getCongestionLevel());
        existingReview.setReviewImageUrl(dto.getReviewImageUrl());

        reviewMapper.updateReview(existingReview);
        return ResponseEntity.ok(Map.of("success", true, "message", "리뷰가 성공적으로 수정되었습니다."));
    }

    // 리뷰 삭제
    @Transactional
    public ResponseEntity<?> deleteReview(Long reviewId, Long userId) {
        
        // 기존 리뷰 존재 여부 확인
        ReviewVO existingReview = reviewMapper.findById(reviewId);
        if (existingReview == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", "존재하지 않는 리뷰입니다."));
        }

        // 권한 검증 본인이 작성한 후기인지 체크 (틀리면 403 Forbidden 리턴)
        if (!existingReview.getUserId().equals(userId)) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "본인이 작성한 후기만 삭제할 수 있습니다."));
        }

        // 도감 테이블에서 이 유저가 이 팝업에서 획득한 키링 데이터 먼저 회수
        reviewMapper.deleteCollectionBook(userId, existingReview.getPopupId());

        // 리뷰 테이블에서 해당 리뷰 삭제 진행
        reviewMapper.deleteReview(reviewId);

        return ResponseEntity.ok(Map.of("success", true, "message", "리뷰가 정상적으로 삭제되었습니다."));
    }
    
    // 판매자용 - 팝업 리뷰 목록 조회
    public ResponseEntity<?> getReviewsByPopup(Long popupId, Long sellerId) {

        String role = reservationMapper.findRoleById(sellerId);
        if (role == null || !role.equals("seller")) {
            return ResponseEntity.status(403)
                .body(Map.of("message", "판매자만 조회할 수 있습니다."));
        }

        if (popupId != null) {
            Long ownerId = reservationMapper.findOwnerByPopupId(popupId);
            if (ownerId == null) {
                return ResponseEntity.status(404)
                    .body(Map.of("message", "존재하지 않는 팝업입니다."));
            }
            if (!ownerId.equals(sellerId)) {
                return ResponseEntity.status(403)
                    .body(Map.of("message", "본인이 등록한 팝업만 조회할 수 있습니다."));
            }
            List<ReviewVO> reviews = reviewMapper.findByPopupId(popupId);
            return ResponseEntity.ok(reviews);
        } else {
            List<AdminReviewResponseDto> reviews = reviewMapper.findAllBySellerId(sellerId);
            return ResponseEntity.ok(reviews);
        }
    }

}
