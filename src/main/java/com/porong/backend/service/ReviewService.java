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
            imageUrl = saveImage(image);
        }

        ReviewVO review = new ReviewVO();
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setCongestionLevel(dto.getCongestionLevel());
        review.setReviewImageUrl(imageUrl);
        review.setPopupId(popupId);
        review.setUserId(dto.getUserId());
        review.setReservationId(dto.getReservationId());

     // 리뷰 인서트 실행
        reviewMapper.insertReview(review);

        // 인서트 직후 유저의 누적 리뷰 개수 확인
        int totalReviewCount = reviewMapper.countReviewsByUserId(dto.getUserId());

        Long earnedKeyringId = null;
        String keyringName = "";
        String responseMessage = totalReviewCount + "번째 리뷰가 성공적으로 등록되었습니다!";

        // 5개 이하일 때만 키링 보상 프로세스 진행
        if (totalReviewCount <= 5) {
            Map<String, Object> keyringMap = reviewMapper.findKeyringByReviewCount(totalReviewCount);

            if (keyringMap != null) {
                earnedKeyringId = ((Number) keyringMap.get("id")).longValue();
                keyringName = (String) keyringMap.get("name");
                
                // 어떤 리뷰(review.getId())로 얻은 키링인지 명시하여 도감에 저장
                reviewMapper.insertCollectionBook(dto.getUserId(), earnedKeyringId, popupId, review.getId());
                responseMessage = totalReviewCount + "번째 리뷰가 등록되었으며, '" + keyringName + "' 키링이 도감에 지급되었습니다!";
            }
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
        
        ReviewVO existingReview = reviewMapper.findById(reviewId);
        if (existingReview == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", "존재하지 않는 리뷰입니다."));
        }

        if (!existingReview.getUserId().equals(userId)) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "본인이 작성한 후기만 삭제할 수 있습니다."));
        }

        // 삭제 직전 이 유저의 총 리뷰 개수 확인
        int currentCount = reviewMapper.countReviewsByUserId(userId);

        // 전체 개수가 5개 이하라면 이 리뷰와 매핑된 도감 키링을 정밀 타겟하여 선제 회수
        if (currentCount <= 5) {
            reviewMapper.deleteCollectionBook(userId, reviewId);
        }

        // 리뷰 삭제는 if 조건과 관계없이 무조건 마지막에 딱 한 번만 깔끔하게 실행
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
    
 // 리뷰 단건 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getReviewById(Long reviewId) {
        // Map을 반환하는 신규 매퍼 메서드 호출
        Map<String, Object> review = reviewMapper.findReviewDetailById(reviewId);
        
        // 예외 처리
        if (review == null || review.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "존재하지 않거나 삭제된 리뷰입니다."));
        }
        
        // 별도의 재가공 없이 매퍼가 뽑아온 데이터를 그대로 반환
        return ResponseEntity.ok(review);
    }

}
