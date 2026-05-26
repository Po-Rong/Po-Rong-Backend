package com.porong.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationVO {
	
    private Long id;
    private Long userId;
    private Long popupId;
    private String reserveDate;
    private String userName;
    private String userPhone;
    private String status;
    private String createdAt;

}
