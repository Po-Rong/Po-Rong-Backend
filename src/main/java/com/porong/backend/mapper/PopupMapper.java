package com.porong.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.porong.backend.vo.PopupVO;

@Mapper
public interface PopupMapper {
	
    // 팝업 등록
    @Insert("INSERT INTO popups (user_id, title, category_id, region_id, address, " +
            "start_date, end_date, reservation_start_date, reservation_end_date, " +
            "notice, benefit, info, sns_url, main_image_url, created_at) " +
            "VALUES (#{userId}, #{title}, #{categoryId}, #{regionId}, #{address}, " +
            "#{startDate}, #{endDate}, #{reservationStartDate}, #{reservationEndDate}, " +
            "#{notice}, #{benefit}, #{info}, #{snsUrl}, #{mainImageUrl}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PopupVO popup);

    // 상세 이미지 등록
    @Insert("INSERT INTO popup_images (popup_id, detail_image_url, image_order) " +
            "VALUES (#{popupId}, #{detailImageUrl}, #{imageOrder})")
    int insertImage(@Param("popupId") Long popupId,
                    @Param("detailImageUrl") String detailImageUrl,
                    @Param("imageOrder") int imageOrder);

    // 태그 등록
    @Insert("INSERT INTO popup_tags (popup_id, tag) VALUES (#{popupId}, #{tag})")
    int insertTag(@Param("popupId") Long popupId, @Param("tag") String tag);

}
