package com.porong.backend.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyWishlistResponseDto {
	
	private Long wishlistId;       // wishlists 테이블의 id
    private Long popupId;          // popups 테이블의 id
    private String title;          // 팝업 제목
    private String mainImageUrl;   // 팝업 메인 이미지 URL
    private String address;        // 팝업 주소
    private String status;         // 팝업 상태 (ongoing, upcoming, closed)
    private LocalDateTime createdAt; // 찜한 시간
    private String regionName;       // regions 테이블에서 가져온 지역 이름
    private String categoryName;     // categories 테이블에서 가져온 카테고리 이름    
    private LocalDateTime startDate; // 운영 시작일
    private LocalDateTime endDate;   // 운영 종료일
}
