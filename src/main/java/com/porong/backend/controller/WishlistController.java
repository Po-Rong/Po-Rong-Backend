package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.dto.request.WishlistRequestDto;
import com.porong.backend.service.WishlistService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WishlistController {
	
	@Autowired
	private WishlistService wishlistService;
	
	// 내 찜 목록 조회
	@GetMapping("/wishlists/me")
	public ResponseEntity<?> getMyWishlist(@RequestParam("user_id") Long userId) {
        return wishlistService.getMyWishlist(userId);
    }
	
	// 팝업 스토어 찜하기 / 찜 해제
	@PostMapping("/popups/{popupId}/wishlist")
    public ResponseEntity<?> toggleWishlist(
            @PathVariable("popupId") Long popupId,
            @RequestBody WishlistRequestDto request) {
        return wishlistService.toggleWishlist(request.getUserId(), popupId);
    }

}
