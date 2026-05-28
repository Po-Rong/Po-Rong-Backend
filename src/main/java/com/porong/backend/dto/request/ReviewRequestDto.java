package com.porong.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
	private Long userId;
    private String content;
    private Integer rating;
    private String congestionLevel;
    private Long reservationId;
    private String reviewImageUrl;
}
