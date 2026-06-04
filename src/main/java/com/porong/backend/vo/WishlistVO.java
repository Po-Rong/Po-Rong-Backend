package com.porong.backend.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistVO {
	
	private Long id;          // 찜 목록 고유 PK (AUTO_INCREMENT)
    private Long popupId;     // 팝업 스토어 ID (외래키)
    private Long userId;      // 회원 ID (외래키)
    private LocalDateTime createdAt; // 찜한 시간 (DEFAULT CURRENT_TIMESTAMP)

}
