package com.porong.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDto {
	private Long reviewId;          // r.id -> 명세서 맞춤 변경
    private Long userId;            // r.user_id -> 명세서 맞춤 추가
    private String nickname;        // u.nickname
    private String reserveDate;     // DB의 DATE 형식을 "YYYY-MM-DD" 문자열로 변환하여 매핑
    private String reserveTime;     // DB의 TIME 형식을 "오전/오후 HH시" 문자열로 변환하여 매핑
    private String congestionLevel; // r.congestion_level
    private String content;         // r.content
    private String reviewImageUrl;  // r.review_image_url
    private Integer rating;
    private Long reservationId;		// 예약 Id
}
