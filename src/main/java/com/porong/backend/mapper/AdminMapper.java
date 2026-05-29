package com.porong.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
	
	// 판매자 여부 체크
	@Select("SELECT role FROM users WHERE id = #{userId}")
	String findRoleById(Long userId);
	
    // 이번달 예약 수
    @Select("SELECT COUNT(*) FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId} " +
            "AND r.status = 'CONFIRMED' " +
            "AND MONTH(r.reserve_date) = MONTH(NOW()) " +
            "AND YEAR(r.reserve_date) = YEAR(NOW())")
    int countMonthlyReservations(Long sellerId);

    // 평균 별점
    @Select("SELECT ROUND(COALESCE(AVG(rv.rating), 0), 1) " +
            "FROM reviews rv " +
            "JOIN popups p ON rv.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId}")
    double getAverageRating(Long sellerId);

    // 총 리뷰 수
    @Select("SELECT COUNT(*) FROM reviews rv " +
            "JOIN popups p ON rv.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId}")
    int countTotalReviews(Long sellerId);
}
