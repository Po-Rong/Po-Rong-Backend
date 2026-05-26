package com.porong.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateResponseDto {
	private boolean success;
    private String message;
    private Long reviewId;
    private Long earnedKeyringId; // 지급된 키링 ID
}
