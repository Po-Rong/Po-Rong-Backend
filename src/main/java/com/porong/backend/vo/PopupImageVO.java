package com.porong.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupImageVO {
    private Long id;
    private Long popupId;
    private String detailImageUrl;
    private int imageOrder;

}
