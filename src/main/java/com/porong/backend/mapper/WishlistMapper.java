package com.porong.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.porong.backend.dto.response.MyWishlistResponseDto;
import com.porong.backend.vo.WishlistVO;

@Mapper
public interface WishlistMapper {
	// 기존 찜 여부 조회
    @Select("SELECT id, popup_id, user_id, created_at FROM wishlists WHERE user_id = #{userId} AND popup_id = #{popupId}")
    WishlistVO selectWishlist(@Param("userId") Long userId, @Param("popupId") Long popupId);
    
    // 찜 추가하기
    @Insert("INSERT INTO wishlists (popup_id, user_id) VALUES (#{popupId}, #{userId})")
    int insertWishlist(@Param("userId") Long userId, @Param("popupId") Long popupId);
    
    // 찜 해제하기
    @Delete("DELETE FROM wishlists WHERE id = #{id}")
    int deleteWishlist(Long id);
    
    // 내 찜 목록 전체 조회
    @Select("""
    	    SELECT
    	        w.id AS wishlistId,
    	        w.popup_id AS popupId,
    	        p.title,
    	        p.main_image_url AS mainImageUrl,
    	        p.address,
    	        CASE
    	            WHEN NOW() < p.start_date THEN 'upcoming'
    	            WHEN NOW() > p.end_date THEN 'closed'
    	            ELSE 'ongoing'
    	        END AS status,
    	        w.created_at AS createdAt,
    	        r.region_name AS regionName,
    	        c.category_name AS categoryName,
    	        p.start_date AS startDate,
    	        p.end_date AS endDate
    	    FROM wishlists w
    	    INNER JOIN popups p ON w.popup_id = p.id
    	    LEFT JOIN regions r ON p.region_id = r.id
    	    LEFT JOIN categories c ON p.category_id = c.id
    	    WHERE w.user_id = #{userId}
    	    ORDER BY w.created_at DESC
    	""")
    	List<MyWishlistResponseDto> selectMyWishlist(Long userId);
    

     // 특정 팝업스토어의 찜 목록 전체 조회
     @Select("""
         SELECT 
             w.id AS wishlistId,
             w.popup_id AS popupId,
             p.title,
             p.main_image_url AS mainImageUrl,
             p.address,
             p.status,
             w.created_at AS createdAt,
             r.region_name AS regionName,
             c.category_name AS categoryName,
             p.start_date AS startDate,
             p.end_date AS endDate
         FROM wishlists w
         INNER JOIN popups p ON w.popup_id = p.id
         LEFT JOIN regions r ON p.region_id = r.id
         LEFT JOIN categories c ON p.category_id = c.id
         WHERE w.popup_id = #{popupId}
         ORDER BY w.created_at DESC
     """)
     List<MyWishlistResponseDto> selectWishlistByPopupId(Long popupId);
}