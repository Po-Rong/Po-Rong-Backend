package com.porong.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupRequestDto {
    private Long sellerId;
    private String title;
    private Long categoryId;
    private Long regionId;
    private String address;
    private String startDate;
    private String endDate;
    private String reservationStartDate;
    private String reservationEndDate;
    private String notice;
    private String benefit;
    private String info;
    private String snsUrl;
}
