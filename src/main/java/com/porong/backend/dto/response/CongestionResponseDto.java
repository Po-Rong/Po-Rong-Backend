package com.porong.backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CongestionResponseDto {
    private Long popupId;
    private String averageCongestionLevel; // 팝업 전체의 평균 혼잡도 (혼잡/보통/여유)
    private int totalReports;              // 통계에 참여한 총 리뷰 개수
}