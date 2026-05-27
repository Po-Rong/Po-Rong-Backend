package com.porong.backend.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "status", "title", "categoryName", "regionName", "address", 
    "mainImageUrl", "detailImages", "tags", "notice", "benefit", "info", 
    "snsUrl", "latitude", "longitude", "startDate", "endDate", 
    "reservationStartDate", "reservationEndDate", "avgRating", "reviewCount", "createdAt"})
public class PopupDetailResponseDto {
    private Long id;
    private String title;
    private String categoryName;
    private String regionName;
    private String address;
    private String mainImageUrl;
    private List<String> detailImages;
    private List<String> tags;
    private String notice;
    private String benefit;
    private String info;
    private String snsUrl;
    private Double latitude;
    private Double longitude;
    private String startDate;
    private String endDate;
    private String reservationStartDate;
    private String reservationEndDate;
    private Double avgRating;
    private Integer reviewCount;
    private String createdAt;

}
