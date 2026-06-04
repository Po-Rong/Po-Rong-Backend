package com.porong.backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateRequestDto {
	private Long userId;
    private String reserveDate; // 변경할 방문 일자 및 시간
    private String userName;    // 변경할 예약자 실명
    private String userPhone;   // 변경할 예약자 연락처
}