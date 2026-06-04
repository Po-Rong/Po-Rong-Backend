package com.porong.backend.service;

import com.porong.backend.mapper.CongestionMapper;
import com.porong.backend.dto.response.CongestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CongestionService {

    private final CongestionMapper congestionMapper;

    public CongestionResponseDto getPopupCongestionAverage(Long popupId) {
        // 1. 해당 팝업에 쌓인 리뷰가 총 몇 개인지 먼저 확인
        int total = congestionMapper.selectTotalReportCount(popupId);

        // 리뷰 데이터가 아예 없는 경우 안전하게 예외 처리
        if (total == 0) {
            return new CongestionResponseDto(popupId, "정보 없음", 0);
        }

        // 2. 혼잡도 점수 평균값 계산 호출 (예: 2.6666...)
        Double avgScore = congestionMapper.selectAverageCongestionScore(popupId);
        
        // 3. 평균 평점 구간에 따라 대표 문자열 매핑
        String avgLevel = "보통";
        if (avgScore != null) {
            if (avgScore >= 2.5) avgLevel = "혼잡";
            else if (avgScore <= 1.5) avgLevel = "여유";
        }

        return new CongestionResponseDto(popupId, avgLevel, total);
    }
}