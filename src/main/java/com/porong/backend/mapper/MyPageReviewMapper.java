package com.porong.backend.mapper;

import com.porong.backend.dto.response.MyPageReviewResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface MyPageReviewMapper {

    // 마이페이지 전용: 내가 작성한 리뷰 5중 조인 쿼리
	@Select("""
            SELECT 
                rv.id AS reviewId, 
                u.nickname AS nickname, 
                DATE_FORMAT(res.reserve_date, '%p %l시') AS reserveTime, 
                rv.rating AS rating, 
                rv.congestion_level AS congestionLevel, 
                rv.content AS content, 
                rv.review_image_url AS reviewImageUrl, 
                DATE_FORMAT(rv.created_at, '%y.%m.%d') AS createdAt, 
                p.id AS popupId, 
                p.title AS popupTitle, 
                p.main_image_url AS popupMainImageUrl, 
                reg.region_name AS regionName, 
                CASE WHEN p.end_date >= NOW() THEN '운영중' 
                     ELSE '종료' END AS popupStatus,
                c.category_name AS categoryName 
            FROM reviews rv 
            INNER JOIN users u ON rv.user_id = u.id 
            INNER JOIN popups p ON rv.popup_id = p.id 
            LEFT JOIN regions reg ON p.region_id = reg.id 
            LEFT JOIN categories c ON p.category_id = c.id 
            -- 📍 [핵심 수정] 유저ID/팝업ID 맵핑 대신, 리뷰에 심겨있는 고유 reservation_id로 정확히 1:1 조인
            LEFT JOIN reservations res ON rv.reservation_id = res.id 
            WHERE rv.user_id = #{userId} 
            ORDER BY rv.created_at DESC
            """)
    List<MyPageReviewResponseDto> selectMyReviews(@Param("userId") Long userId);
}