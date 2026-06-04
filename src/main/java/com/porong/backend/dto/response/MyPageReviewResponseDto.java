package com.porong.backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageReviewResponseDto {
    private Long reviewId;
    private String nickname;
    private String reserveTime;
    private int rating;
    private String congestionLevel;
    private String content;
    private String reviewImageUrl;
    private String createdAt;

    private Long popupId;
    private String popupTitle;
    private String popupMainImageUrl;
    private String regionName;
    private String popupStatus;
    private String categoryName;
}