package com.porong.backend.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
