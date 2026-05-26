package com.porong.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.porong.backend.dto.request.PopupRequestDto;
import com.porong.backend.mapper.PopupMapper;
import com.porong.backend.vo.PopupVO;

@Service
public class PopupService {
	
	@Autowired
    private PopupMapper popupMapper;

    public ResponseEntity<?> createPopup(PopupRequestDto dto, MultipartFile mainImage,
                                          List<MultipartFile> detailImages, List<String> tags) {

        // 1. 예약 날짜 없으면 운영 날짜로 자동 설정
        if (dto.getReservationStartDate() == null || dto.getReservationStartDate().isEmpty()) {
            dto.setReservationStartDate(dto.getStartDate());
        }
        if (dto.getReservationEndDate() == null || dto.getReservationEndDate().isEmpty()) {
            dto.setReservationEndDate(dto.getEndDate());
        }

        // 2. 날짜 역전 체크
        if (dto.getEndDate().compareTo(dto.getStartDate()) < 0) {
            return ResponseEntity.status(400)
                .body(Map.of("message", "종료일은 시작일보다 빨리 설정할 수 없습니다."));
        }

        // 3. 예약 날짜 역전 체크
        if (dto.getReservationEndDate().compareTo(dto.getReservationStartDate()) < 0) {
            return ResponseEntity.status(400)
                .body(Map.of("message", "예약 종료일은 예약 시작일보다 빨리 설정할 수 없습니다."));
        }

        // 4. 메인 이미지 필수 체크
        if (mainImage == null || mainImage.isEmpty()) {
            return ResponseEntity.status(400)
                .body(Map.of("message", "메인 이미지는 필수입니다."));
        }

        // 5. 메인 이미지 저장
        String mainImageUrl = saveImage(mainImage);

        // 6. VO에 담기
        PopupVO popup = new PopupVO();
        popup.setUserId(dto.getSellerId());
        popup.setTitle(dto.getTitle());
        popup.setCategoryId(dto.getCategoryId());
        popup.setRegionId(dto.getRegionId());
        popup.setAddress(dto.getAddress());
        popup.setStartDate(dto.getStartDate());
        popup.setEndDate(dto.getEndDate());
        popup.setReservationStartDate(dto.getReservationStartDate());
        popup.setReservationEndDate(dto.getReservationEndDate());
        popup.setNotice(dto.getNotice());
        popup.setBenefit(dto.getBenefit());
        popup.setInfo(dto.getInfo());
        popup.setSnsUrl(dto.getSnsUrl());
        popup.setMainImageUrl(mainImageUrl);

        // 7. 팝업 DB 저장
        popupMapper.insert(popup);

        // 8. 상세 이미지 저장
        if (detailImages != null && !detailImages.isEmpty()) {
            for (int i = 0; i < detailImages.size(); i++) {
                String detailImageUrl = saveImage(detailImages.get(i));
                popupMapper.insertImage(popup.getId(), detailImageUrl, i + 1);
            }
        }

        // 9. 태그 저장
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                popupMapper.insertTag(popup.getId(), tag);
            }
        }

        return ResponseEntity.status(201)
            .body(Map.of("id", popup.getId(), "message", "팝업이 등록되었습니다."));
    }

    // 이미지 저장 메서드
    private String saveImage(MultipartFile file) {
        try {
            // 프로젝트 루트 경로 기준으로 uploads 폴더 절대 경로 설정
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            
            // uploads 폴더 없으면 자동 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            
            file.transferTo(new File(filePath));
            
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
    
    
    public ResponseEntity<?> updatePopup(Long id, PopupRequestDto dto, MultipartFile mainImage,
            List<MultipartFile> detailImages, List<String> tags) {

		// 1. 팝업 존재 여부 체크
		PopupVO popup = popupMapper.findById(id);
		if (popup == null) {
		return ResponseEntity.status(404)
		.body(Map.of("message", "존재하지 않는 팝업입니다."));
		}
		
		// 2. 판매자 권한 체크
		if (!popup.getUserId().equals(dto.getSellerId())) {
		return ResponseEntity.status(403)
		.body(Map.of("message", "본인이 등록한 팝업만 수정할 수 있습니다."));
		}
		
		// 3. 예약 날짜 없으면 운영 날짜로 자동 설정
		if (dto.getReservationStartDate() == null || dto.getReservationStartDate().isEmpty()) {
		dto.setReservationStartDate(dto.getStartDate());
		}
		if (dto.getReservationEndDate() == null || dto.getReservationEndDate().isEmpty()) {
		dto.setReservationEndDate(dto.getEndDate());
		}
		
		// 4. 날짜 역전 체크
		if (dto.getEndDate().compareTo(dto.getStartDate()) < 0) {
		return ResponseEntity.status(400)
		.body(Map.of("message", "종료일은 시작일보다 빨리 설정할 수 없습니다."));
		}
		
		// 5. 예약 날짜 역전 체크
		if (dto.getReservationEndDate().compareTo(dto.getReservationStartDate()) < 0) {
		return ResponseEntity.status(400)
		.body(Map.of("message", "예약 종료일은 예약 시작일보다 빨리 설정할 수 없습니다."));
		}
		
		// 6. 메인 이미지 저장 (새 이미지 있으면 교체, 없으면 기존 유지)
		String mainImageUrl = popup.getMainImageUrl();
		if (mainImage != null && !mainImage.isEmpty()) {
		mainImageUrl = saveImage(mainImage);
		}
		
		// 7. VO에 담기
		popup.setTitle(dto.getTitle());
		popup.setCategoryId(dto.getCategoryId());
		popup.setRegionId(dto.getRegionId());
		popup.setAddress(dto.getAddress());
		popup.setStartDate(dto.getStartDate());
		popup.setEndDate(dto.getEndDate());
		popup.setReservationStartDate(dto.getReservationStartDate());
		popup.setReservationEndDate(dto.getReservationEndDate());
		popup.setNotice(dto.getNotice());
		popup.setBenefit(dto.getBenefit());
		popup.setInfo(dto.getInfo());
		popup.setSnsUrl(dto.getSnsUrl());
		popup.setMainImageUrl(mainImageUrl);
		
		// 8. 팝업 수정
		popupMapper.update(popup);
		
		// 9. 상세 이미지 수정 (기존 삭제 후 재등록)
		popupMapper.deleteImages(id);
		if (detailImages != null && !detailImages.isEmpty()) {
		for (int i = 0; i < detailImages.size(); i++) {
		String detailImageUrl = saveImage(detailImages.get(i));
		popupMapper.insertImage(id, detailImageUrl, i + 1);
		}
		}
		
		// 10. 태그 수정 (기존 삭제 후 재등록)
		popupMapper.deleteTags(id);
		if (tags != null && !tags.isEmpty()) {
		for (String tag : tags) {
		popupMapper.insertTag(id, tag);
		}
		}
		
		return ResponseEntity.ok(Map.of("id", id, "message", "팝업이 수정되었습니다."));
		}
}