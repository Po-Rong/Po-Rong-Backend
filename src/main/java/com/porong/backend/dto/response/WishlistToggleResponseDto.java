package com.porong.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistToggleResponseDto {
	
	private boolean success;
    private String message;
    
    private Boolean isWished; // 찜 등록되면 true, 해제되면 false
}
