package com.porong.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.porong.backend.dto.request.PopupRequestDto;
import com.porong.backend.service.PopupService;

@RestController
@RequestMapping("/api/popups")
@CrossOrigin(origins="*")
public class PopupController {
	
    @Autowired
    private PopupService popupService;
    
    // 팝업 등록
    @PostMapping
    public ResponseEntity<?> createPopup(
        @RequestParam("sellerId") Long sellerId,
        @RequestParam("title") String title,
        @RequestParam("categoryId") Long categoryId,
        @RequestParam("regionId") Long regionId,
        @RequestParam("address") String address,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        @RequestParam(value = "reservationStartDate", required = false) String reservationStartDate,
        @RequestParam(value = "reservationEndDate", required = false) String reservationEndDate,
        @RequestParam(value = "notice", required = false) String notice,
        @RequestParam(value = "benefit", required = false) String benefit,
        @RequestParam(value = "info", required = false) String info,
        @RequestParam(value = "snsUrl", required = false) String snsUrl,
        @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
        @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImages,
        @RequestParam(value = "tags", required = false) List<String> tags) {

        PopupRequestDto dto = new PopupRequestDto();
        dto.setSellerId(sellerId);
        dto.setTitle(title);
        dto.setCategoryId(categoryId);
        dto.setRegionId(regionId);
        dto.setAddress(address);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setReservationStartDate(reservationStartDate);
        dto.setReservationEndDate(reservationEndDate);
        dto.setNotice(notice);
        dto.setBenefit(benefit);
        dto.setInfo(info);
        dto.setSnsUrl(snsUrl);

        return popupService.createPopup(dto, mainImage, detailImages, tags);
    }
    
    // 팝업 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePopup(
        @PathVariable("id") Long id,
        @RequestParam("sellerId") Long sellerId,
        @RequestParam("title") String title,
        @RequestParam("categoryId") Long categoryId,
        @RequestParam("regionId") Long regionId,
        @RequestParam("address") String address,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        @RequestParam(value = "reservationStartDate", required = false) String reservationStartDate,
        @RequestParam(value = "reservationEndDate", required = false) String reservationEndDate,
        @RequestParam(value = "notice", required = false) String notice,
        @RequestParam(value = "benefit", required = false) String benefit,
        @RequestParam(value = "info", required = false) String info,
        @RequestParam(value = "snsUrl", required = false) String snsUrl,
        @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
        @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImages,
        @RequestParam(value = "tags", required = false) List<String> tags) {

        PopupRequestDto dto = new PopupRequestDto();
        dto.setSellerId(sellerId);
        dto.setTitle(title);
        dto.setCategoryId(categoryId);
        dto.setRegionId(regionId);
        dto.setAddress(address);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setReservationStartDate(reservationStartDate);
        dto.setReservationEndDate(reservationEndDate);
        dto.setNotice(notice);
        dto.setBenefit(benefit);
        dto.setInfo(info);
        dto.setSnsUrl(snsUrl);

        return popupService.updatePopup(id, dto, mainImage, detailImages, tags);
    }
    
    // 팝업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePopup(
        @PathVariable("id") Long id,
        @RequestParam("seller_id") Long sellerId) {
        return popupService.deletePopup(id, sellerId);
    }
}
