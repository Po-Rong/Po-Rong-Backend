package com.porong.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> createReview(Long popupId, ReviewRequestDto dto, MultipartFile image) {

        // 이미지 파일 처리 (있을 때만)
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImage(image); // 아래 저장 메서드
        }

        int currentReviewCount = reviewMapper.countReviewsByUserId(dto.getUserId());
        int nextReviewCount = currentReviewCount + 1;

        ReviewVO review = new ReviewVO();
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setCongestionLevel(dto.getCongestionLevel());
        review.setReviewImageUrl(imageUrl); // DTO 대신 저장된 경로 사용
        review.setPopupId(popupId);
        review.setUserId(dto.getUserId());
        review.setReservationId(dto.getReservationId());

        reviewMapper.insertReview(review);

        Map<String, Object> keyringMap = reviewMapper.findKeyringByReviewCount(nextReviewCount);

        Long earnedKeyringId = null;
        String keyringName = "";
        String responseMessage = nextReviewCount + "번째 리뷰가 성공적으로 등록되었습니다!";

        if (keyringMap != null) {
            earnedKeyringId = ((Number) keyringMap.get("id")).longValue();
            keyringName = (String) keyringMap.get("name");
            reviewMapper.insertCollectionBook(dto.getUserId(), earnedKeyringId, popupId);
            responseMessage = nextReviewCount + "번째 리뷰가 등록되었으며, '" + keyringName + "' 키링이 도감에 지급되었습니다!";
        }

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
    public ResponseEntity<?> updateReview(Long reviewId, ReviewRequestDto dto, MultipartFile image) {
        ReviewVO existingReview = reviewMapper.findById(reviewId);
        if (existingReview == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", "존재하지 않는 리뷰입니다."));
        }

        if (!existingReview.getUserId().equals(dto.getUserId())) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "본인이 작성한 후기만 수정할 수 있습니다."));
        }

        // 새 이미지가 있으면 교체, 없으면 기존 이미지 유지
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            existingReview.setReviewImageUrl(imageUrl);
        }

        existingReview.setContent(dto.getContent());
        existingReview.setRating(dto.getRating());
        existingReview.setCongestionLevel(dto.getCongestionLevel());

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
    
    // 최근 리뷰 3건 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getRecentThreeReviews() {
        List<Map<String, Object>> recentReviews = reviewMapper.selectRecentThreeReviews();
        return ResponseEntity.ok(recentReviews);
    }
    
    // 이미지 저장 메서드
    private String saveImage(MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 쿼리스트링 제거
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains("?")) {
                originalFilename = originalFilename.split("\\?")[0];
            }
            
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            String filePath = uploadDir + fileName;
            
            file.transferTo(new File(filePath));
            
            return "http://localhost:8080/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

}
