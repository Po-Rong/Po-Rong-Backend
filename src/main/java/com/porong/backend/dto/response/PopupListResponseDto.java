package com.porong.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "status", "title", "categoryName", "regionName", "mainImageUrl", "startDate", "endDate", "avgRating", "isWishlisted"})
public class PopupListResponseDto {
	
    private Long id;
    private String status;
    private String title;
    private String categoryName;
    private String regionName;
    private String mainImageUrl;
    private String startDate;
    private String endDate;
    private Double avgRating;
    private Boolean isWishlisted;
}
