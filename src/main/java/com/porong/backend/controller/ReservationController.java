package com.porong.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.dto.request.ReservationCancelRequestDto;
import com.porong.backend.dto.request.ReservationCreateRequestDto;
import com.porong.backend.dto.request.ReservationUpdateRequestDto;
import com.porong.backend.dto.response.ReservationResponseDto;
import com.porong.backend.service.ReservationService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations") // 공통 경로를 클래스 레벨로 묶어줍니다!
public class ReservationController {

    private final ReservationService reservationService;

    // 1. 팝업 예약 신청 (공통 경로인 /api/reservations에 영향을 받지 않도록 전체 경로 지정)
    @PostMapping("/popups/{popupId}")
    public ResponseEntity<?> groupReservation(@PathVariable("popupId") Long popupId, @RequestBody ReservationCreateRequestDto request) {
        reservationService.createReservation(popupId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "예약이 성공적으로 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 2. 내 예약 조회 ➔ 실제 경로: GET /api/reservations/me
    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(@RequestParam("user_id") Long userId) {
        List<ReservationResponseDto> list = reservationService.getMyReservations(userId);
        return ResponseEntity.ok(list);
    }

 // 3. 팝업 예약 취소
    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable("reservationId") Long reservationId, @RequestBody ReservationCancelRequestDto request) {
        String result = reservationService.cancelReservation(reservationId, request);
        
        Map<String, Object> response = new HashMap<>();
        
        if ("NOT_FOUND".equals(result)) {
            response.put("success", false);
            response.put("message", "해당 예약을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        if ("NOT_OWNER".equals(result)) {
            response.put("success", false);
            response.put("message", "본인이 신청한 예약만 취소할 수 있습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("success", true);
        response.put("message", "예약이 성공적으로 취소되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 4. 팝업 예약 수정
    @PatchMapping("/{reservationId}")
    public ResponseEntity<?> updateReservation(@PathVariable("reservationId") Long reservationId, @RequestBody ReservationUpdateRequestDto request) {
        String result = reservationService.updateReservation(reservationId, request);
        
        Map<String, Object> response = new HashMap<>();
        
        if ("NOT_FOUND".equals(result)) {
            response.put("success", false);
            response.put("message", "해당 예약을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        if ("NOT_OWNER".equals(result)) {
            response.put("success", false);
            response.put("message", "본인이 신청한 예약만 수정할 수 있습니다."); // 수정 문맥에 맞춤
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("success", true);
        response.put("message", "예약 정보가 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(response);
    
    }
    
    // 판매자용 - 팝업 예약자 목록 조회
    @GetMapping
    public ResponseEntity<?> getReservationsByPopup(
        @RequestParam("popup_id") Long popupId,
        @RequestParam("seller_id") Long sellerId) {
        return reservationService.getReservationsByPopup(popupId, sellerId);
    }
}