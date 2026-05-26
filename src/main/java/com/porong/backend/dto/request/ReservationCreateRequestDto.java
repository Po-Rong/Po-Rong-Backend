package com.porong.backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateRequestDto {
    private Long userId;        // 예약하는 유저의 고유 ID
    private String reserveDate; // 방문 예정 일자 및 시간
    private String userName;    // 예약자 실명
    private String userPhone;   // 예약자 연락처
}