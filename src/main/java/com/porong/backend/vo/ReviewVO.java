package com.porong.backend.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewVO {

	private Long id;                 // 리뷰 고유 PK (AUTO_INCREMENT)
    private String content;          // 후기 텍스트 내용
    private Integer rating;          // 별점 (1~5 사이의 정수)
    private String congestionLevel;  // 실시간 혼잡도 (HIGH / NORMAL / LOW)
    private String reviewImageUrl;   // 리뷰 이미지 파일 URL (NULL 허용)
    private Long popupId;            // 대상 팝업 스토어 ID (외래키)
    private Long userId;             // 작성자 회원 ID (외래키)
    private Long reservationId;		// 예약 ID (외래키)
    private LocalDateTime createdAt; // 작성 시간 (DEFAULT CURRENT_TIMESTAMP)
}
