package com.porong.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porong.backend.dto.response.MyWishlistResponseDto;
import com.porong.backend.dto.response.WishlistToggleResponseDto;
import com.porong.backend.mapper.WishlistMapper;
import com.porong.backend.vo.WishlistVO;

@Service
public class WishlistService {
	
	@Autowired
    private WishlistMapper wishlistMapper;

//	팝업 찜하기 토글 (POST api/popups/{popupId}/wishlist)
//	기존에 찜한 내역이 있으면 삭제(해제), 없으면 추가(등록)를 진행합니다.
    @Transactional
    public ResponseEntity<?> toggleWishlist(Long userId, Long popupId) {
        
        // 이미 찜해둔 내역이 있는지 조회
        WishlistVO existingWishlist = wishlistMapper.selectWishlist(userId, popupId);
        
        // 분기 처리 (토글 로직)
        if (existingWishlist != null) {
            // 이미 존재하면 -> 찜 해제 (Delete)
            wishlistMapper.deleteWishlist(existingWishlist.getId());
            
            WishlistToggleResponseDto response = WishlistToggleResponseDto.builder()
                    .success(true)
                    .message("팝업 스토어를 찜 목록에서 삭제했습니다.")
                    .isWished(false)
                    .build();
                    
            return ResponseEntity.ok(response);
            
        } else {
            // 존재하지 않으면 -> 찜 추가 (Insert)
            wishlistMapper.insertWishlist(userId, popupId);
            
            WishlistToggleResponseDto response = WishlistToggleResponseDto.builder()
                    .success(true)
                    .message("팝업 스토어를 찜 목록에 추가했습니다.")
                    .isWished(true)
                    .build();
                    
            return ResponseEntity.ok(response);
        }
    }


//  내 찜 목록 전체 조회 (GET /api/wishlists/me?user_id=1)

    @Transactional(readOnly = true)
    public ResponseEntity<?> getMyWishlist(Long userId) {
        
        // 1. Mapper를 통해 팝업 정보와 JOIN된 리스트 조회
        List<MyWishlistResponseDto> wishlist = wishlistMapper.selectMyWishlist(userId);
        
        // 2. 응답 반환 (명세서의 배열 형태로 200 OK 리턴)
        return ResponseEntity.ok(wishlist);
    }
    
 // 특정 팝업스토어 찜 목록 조회 (GET /api/wishlists/popups/{popupId})
    @Transactional(readOnly = true)
    public ResponseEntity<List<MyWishlistResponseDto>> getWishlistByPopupId(Long popupId) {
        
        // 1. Mapper를 통해 해당 팝업을 찜한 목록 조회
        List<MyWishlistResponseDto> wishlist = wishlistMapper.selectWishlistByPopupId(popupId);
        
        // 2. 응답 반환
        return ResponseEntity.ok(wishlist);
    }

}
