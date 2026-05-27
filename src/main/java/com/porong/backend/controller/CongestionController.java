package com.porong.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.porong.backend.dto.response.CongestionResponseDto;
import com.porong.backend.service.CongestionService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popups")
public class CongestionController {

    private final CongestionService congestionService;

    // 최종 확정: 오직 해당 팝업의 평균 혼잡도 통계만 반환하는 API
    @GetMapping("/{popupId}/congestion")
    public ResponseEntity<CongestionResponseDto> getPopupCongestion(@PathVariable("popupId") Long popupId) {
        CongestionResponseDto responseDto = congestionService.getPopupCongestionAverage(popupId);
        return ResponseEntity.ok(responseDto);
    }
}