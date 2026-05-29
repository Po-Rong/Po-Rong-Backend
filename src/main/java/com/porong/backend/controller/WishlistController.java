package com.porong.backend.controller;

import java.util.List;

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
import com.porong.backend.dto.response.MyWishlistResponseDto;
import com.porong.backend.service.WishlistService;

@RestController
@RequestMapping("/api/wishlists")
@CrossOrigin(origins = "*")
public class WishlistController {
	
	@Autowired
	private WishlistService wishlistService;
	
	// 내 찜 목록 조회
	@GetMapping("/me")
	public ResponseEntity<?> getMyWishlist(@RequestParam("user_id") Long userId) {
        return wishlistService.getMyWishlist(userId);
    }
	
	// 팝업 스토어 찜하기 / 찜 해제
	@PostMapping("/popups/{popupId}")
    public ResponseEntity<?> toggleWishlist(
            @PathVariable("popupId") Long popupId,
            @RequestBody WishlistRequestDto request) {
        return wishlistService.toggleWishlist(request.getUserId(), popupId);
    }
	
	// 특정 팝업스토어의 찜 정보 조회
	@GetMapping("/popups/{popupId}")
	public ResponseEntity<List<MyWishlistResponseDto>> getWishlistByPopupId(@PathVariable("popupId") Long popupId) {
	    return wishlistService.getWishlistByPopupId(popupId);
	}

}
