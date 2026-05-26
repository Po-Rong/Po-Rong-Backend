package com.porong.backend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCancelRequestDto {
    private Long userId;        // 취소를 요청한 유저 ID (본인 검증용)
}