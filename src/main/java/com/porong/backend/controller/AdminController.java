package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.service.AdminService;
import com.porong.backend.service.ReviewService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins="*")
public class AdminController {
	
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private AdminService adminService;

    // 판매자용 - 팝업 리뷰 목록 조회
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewsByPopup(
        @RequestParam(value = "popup_id", required = false) Long popupId,
        @RequestParam("seller_id") Long sellerId) {
        return reviewService.getReviewsByPopup(popupId, sellerId);
    }
    
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestParam("seller_id") Long sellerId) {
        return adminService.getSummary(sellerId);
    }

}
