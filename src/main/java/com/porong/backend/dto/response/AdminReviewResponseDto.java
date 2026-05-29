package com.porong.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "userId", "nickname", "rating", "content", "congestionLevel", 
	"reviewImageUrl", "createdAt", "popupTitle", "popupMainImageUrl", "popupStatus", "regionName", "categoryName"})
public class AdminReviewResponseDto {
    private Long id;
    private Long userId;
    private String nickname;
    private Integer rating;
    private String content;
    private String congestionLevel;
    private String reviewImageUrl;
    private String createdAt;
    private String popupTitle;
    private String popupMainImageUrl;
    private String categoryName;
    private String popupStatus;   // 팝업 운영 상태
    private String regionName;    // 지역명

}
