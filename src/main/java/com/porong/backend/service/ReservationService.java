package com.porong.backend.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porong.backend.dto.request.ReservationCancelRequestDto;
import com.porong.backend.dto.request.ReservationCreateRequestDto;
import com.porong.backend.dto.request.ReservationUpdateRequestDto;
import com.porong.backend.dto.response.ReservationResponseDto;
import com.porong.backend.mapper.ReservationMapper;
import com.porong.backend.vo.ReservationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;

    // 1. 팝업 예약 신청
    @Transactional
    public void createReservation(Long popupId, ReservationCreateRequestDto request) {
        reservationMapper.insertReservation(popupId, request);
    }

    // 2. 내 예약 내역 전체 조회
    public List<ReservationResponseDto> getMyReservations(Long userId) {
        return reservationMapper.selectMyReservations(userId);
    }

    // 3. [수정 완료] 팝업 예약 취소 (명세서 실패 조건 반영)
    @Transactional
    public String cancelReservation(Long reservationId, ReservationCancelRequestDto request) {
        ReservationResponseDto exist = reservationMapper.selectReservationById(reservationId);
        
        // 조건 1: 해당 예약이 존재하지 않을 때
        if (exist == null) {
            return "NOT_FOUND";
        }
        
        // 조건 2: 예약은 있지만, 신청한 본인이 아닐 때 (exist의 userId와 요청의 userId 비교)
        if (!exist.getUserId().equals(request.getUserId())) {
            return "NOT_OWNER";
        }
        
        reservationMapper.cancelReservation(reservationId);
        return "SUCCESS";
    }

    // 4. 팝업 예약 정보 수정 (명세서 실패 조건 반영)
    @Transactional
    public String updateReservation(Long reservationId, ReservationUpdateRequestDto request) {
        ReservationResponseDto exist = reservationMapper.selectReservationById(reservationId);
        
        // 조건 1: 해당 예약이 존재하지 않을 때
        if (exist == null) {
            return "NOT_FOUND";
        }
        
        // 조건 2: 예약은 있지만, 신청한 본인이 아닐 때 (exist의 userId와 요청의 userId 비교)
        if (!exist.getUserId().equals(request.getUserId())) {
            return "NOT_OWNER";
        }
        
        reservationMapper.updateReservation(reservationId, request);
        return "SUCCESS";
    }
    
    // 5. 판매자용 - 팝업 예약자 목록 조회
    public ResponseEntity<?> getReservationsByPopup(Long popupId, Long sellerId, int page, int size) {
        String role = reservationMapper.findRoleById(sellerId);
        if (role == null || !role.equals("seller")) {
            return ResponseEntity.status(403)
                .body(Map.of("message", "판매자만 조회할 수 있습니다."));
        }

        int offset = page * size;
        List<String> dates = reservationMapper.findDistinctDatesBySellerId(sellerId, size, offset);
        int totalDates = reservationMapper.countDistinctDatesBySellerId(sellerId);
        boolean hasNext = (offset + size) < totalDates;

        List<Map<String, Object>> result = new ArrayList<>();
        for (String date : dates) {
            List<ReservationVO> reservations = reservationMapper.findBySellerIdAndDate(sellerId, date);
            Map<String, Object> group = new LinkedHashMap<>();
            group.put("date", date);
            group.put("reservations", reservations);
            result.add(group);
        }

        return ResponseEntity.ok(Map.of(
            "content", result,
            "currentPage", page,
            "hasNext", hasNext
        ));
    }
}