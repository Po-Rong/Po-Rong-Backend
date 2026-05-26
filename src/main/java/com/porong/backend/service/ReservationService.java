package com.porong.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porong.backend.dto.request.ReservationCancelRequestDto;
import com.porong.backend.dto.request.ReservationCreateRequestDto;
import com.porong.backend.dto.request.ReservationUpdateRequestDto;
import com.porong.backend.dto.response.ReservationResponseDto;
import com.porong.backend.mapper.ReservationMapper;

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
}