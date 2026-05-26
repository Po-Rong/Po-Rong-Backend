package com.porong.backend.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

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
    
    // 팝업 수정
    @Update("UPDATE popups SET title=#{title}, category_id=#{categoryId}, region_id=#{regionId}, " +
            "address=#{address}, start_date=#{startDate}, end_date=#{endDate}, " +
            "reservation_start_date=#{reservationStartDate}, reservation_end_date=#{reservationEndDate}, " +
            "notice=#{notice}, benefit=#{benefit}, info=#{info}, sns_url=#{snsUrl}, " +
            "main_image_url=#{mainImageUrl} WHERE id=#{id}")
    int update(PopupVO popup);

    // 팝업 ID로 조회
    @Select("SELECT * FROM popups WHERE id = #{id}")
    PopupVO findById(Long id);

    // 상세 이미지 삭제 (수정 시 기존 이미지 삭제 후 재등록)
    @Delete("DELETE FROM popup_images WHERE popup_id = #{popupId}")
    int deleteImages(Long popupId);

    // 태그 삭제 (수정 시 기존 태그 삭제 후 재등록)
    @Delete("DELETE FROM popup_tags WHERE popup_id = #{popupId}")
    int deleteTags(Long popupId);
    
    // 팝업 삭제
    @Delete("DELETE FROM popups WHERE id = #{id}")
    int delete(Long id);

    // 예약자 존재 여부 체크
    @Select("SELECT COUNT(*) FROM reservations WHERE popup_id = #{popupId} AND status = 'CONFIRMED'")
    int countConfirmedReservations(Long popupId);
    
    @SelectProvider(type = PopupSqlProvider.class, method = "getPopupList")
    List<Map<String, Object>> getPopupList(Map<String, Object> params);

}
