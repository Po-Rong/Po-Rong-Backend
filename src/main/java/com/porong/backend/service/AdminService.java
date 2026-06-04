package com.porong.backend.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.porong.backend.mapper.AdminMapper;

@Service
public class AdminService {
	
    @Autowired
    private AdminMapper adminMapper;
    
    public ResponseEntity<?> getSummary(Long sellerId) {
        String role = adminMapper.findRoleById(sellerId);
        if (role == null || !role.equals("seller")) {
            return ResponseEntity.status(403)
                .body(Map.of("message", "판매자만 조회할 수 있습니다."));
        }

        int monthlyReservationCount = adminMapper.countMonthlyReservations(sellerId);
        double averageRating = adminMapper.getAverageRating(sellerId);
        int totalReviewCount = adminMapper.countTotalReviews(sellerId);

        return ResponseEntity.ok(Map.of(
            "monthlyReservationCount", monthlyReservationCount,
            "averageRating", averageRating,
            "totalReviewCount", totalReviewCount
        ));
    }
}
