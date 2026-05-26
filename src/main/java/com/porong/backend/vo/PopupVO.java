package com.porong.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupVO {
    private Long id;
    private Long userId;
    private String title;
    private String address;
    private Long regionId;
    private Long categoryId;
    private String notice;
    private String benefit;
    private String info;
    private String mainImageUrl;
    private String snsUrl;
    private String keyringImageUrl;
    private Double longitude;
    private Double latitude;
    private String startDate;
    private String endDate;
    private String reservationStartDate;
    private String reservationEndDate;
    private String createdAt;
}
