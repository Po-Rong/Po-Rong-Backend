package com.porong.backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
	private Long userId;        // 유저 ID
    private Long id;            // 예약 고유 번호 (PK)
    private Long popupId;       // 팝업스토어 고유 ID
    private String popupTitle;  // 팝업스토어 이름
    private String mainImageUrl;// 팝업 대표 이미지 URL
    private String reserveDate; // 예약 방문 일자
    private String status;      // 예약 상태 (CONFIRMED, CANCELED 등)
    private String userName;    // 예약자 이름
    private String userPhone;   // 예약자 전화번호
    private String createdAt;   // 예약 신청 일시
}