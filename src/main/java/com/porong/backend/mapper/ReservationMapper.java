package com.porong.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.porong.backend.dto.request.ReservationCreateRequestDto;
import com.porong.backend.dto.request.ReservationUpdateRequestDto;
import com.porong.backend.dto.response.ReservationResponseDto;
import com.porong.backend.vo.ReservationVO;

@Mapper
public interface ReservationMapper {

    // 팝업 예약 신청
	@Insert("INSERT INTO reservations (popup_id, user_id, reserve_date, user_name, user_phone, status, created_at) " +
            "VALUES (#{popupId}, #{req.userId}, #{req.reserveDate}, #{req.userName}, #{req.userPhone}, 'CONFIRMED', NOW())")
    int insertReservation(@Param("popupId") Long popupId, @Param("req") ReservationCreateRequestDto request);

    // 내 예약 내역 조회
    @Select("SELECT r.id, " +
            "       r.user_id AS userId, " + 
            "       r.popup_id AS popupId, " +
            "       p.title AS popupTitle, " +
            "       p.main_image_url AS mainImageUrl, " +
            "       r.reserve_date AS reserveDate, " +
            "       r.status, " +
            "       r.user_name AS userName, " +
            "       r.user_phone AS userPhone, " +
            "       r.created_at AS createdAt, " +
            "       CASE WHEN rv.id IS NOT NULL THEN TRUE ELSE FALSE END AS isReviewed " +
            "FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "LEFT JOIN reviews rv ON r.id = rv.reservation_id " +
            "WHERE r.user_id = #{userId} " +
            "ORDER BY r.created_at DESC")
    List<ReservationResponseDto> selectMyReservations(@Param("userId") Long userId);

    // 본인 검증 및 확인을 위한 단건 조회 쿼리
    @Select("SELECT r.id, r.user_id AS userId, r.status, " +
            "       CASE WHEN rv.id IS NOT NULL THEN TRUE ELSE FALSE END AS isReviewed " +
            "FROM reservations r " +
            "LEFT JOIN reviews rv ON r.id = rv.reservation_id " +
            "WHERE r.id = #{reservationId}")
    ReservationResponseDto selectReservationById(@Param("reservationId") Long reservationId);
    
    // 팝업 예약 취소 (상태값만 CANCELED로 변경)
    @Update("UPDATE reservations SET status = 'CANCELED' WHERE id = #{reservationId}")
    int cancelReservation(@Param("reservationId") Long reservationId);

    // 팝업 예약 정보 수정
    @Update("UPDATE reservations SET reserve_date = #{req.reserveDate}, user_name = #{req.userName}, user_phone = #{req.userPhone} " +
            "WHERE id = #{reservationId}")
    int updateReservation(@Param("reservationId") Long reservationId, @Param("req") ReservationUpdateRequestDto request);
    
    // 판매자용 - 팝업 예약자 목록 조회
    @Select("SELECT * FROM reservations WHERE popup_id = #{popupId}")
    List<ReservationVO> findByPopupId(Long popupId);

    // 판매자 여부 체크
    @Select("SELECT role FROM users WHERE id = #{userId}")
    String findRoleById(Long userId);

    // 팝업 소유자 체크
    @Select("SELECT user_id FROM popups WHERE id = #{popupId}")
    Long findOwnerByPopupId(Long popupId);
    
    // 판매자 전체 팝업 예약자 조회
    @Select("SELECT r.* FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId}")
    List<ReservationVO> findAllBySellerId(Long sellerId);


    // 판매자 팝업의 특정 날짜 예약자 목록 조회
    @Select("SELECT r.*, p.title as popupTitle, p.main_image_url as mainImageUrl " +
            "FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId} " +
            "AND DATE(r.reserve_date) = #{date} " +
            "AND r.status IN ('CONFIRMED', 'CANCELED')")
    List<ReservationResponseDto> findBySellerIdAndDate(@Param("sellerId") Long sellerId,
                                                        @Param("date") String date);

    // 예약 단건 조회
    @Select("SELECT * FROM reservations WHERE id = #{reservationId}")
    ReservationVO findById(Long reservationId);

    // 판매자 예약 취소
    @Update("UPDATE reservations SET status = 'CANCELED' WHERE id = #{reservationId}")
    void cancelReservationBySeller(Long reservationId);
    
    // 판매자 팝업의 예약 날짜 목록 페이징 조회 (년/월 필터, 날짜 중복 제거, 오름차순)
    @Select("SELECT DISTINCT DATE(reserve_date) as date " +
            "FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId} " +
            "AND r.status IN ('CONFIRMED', 'CANCELED') " +
            "AND YEAR(r.reserve_date) = #{year} " +
            "AND MONTH(r.reserve_date) = #{month} " +
            "ORDER BY date ASC " +
            "LIMIT #{size} OFFSET #{offset}")
    List<String> findDistinctDatesBySellerId(@Param("sellerId") Long sellerId,
                                              @Param("year") int year,
                                              @Param("month") int month,
                                              @Param("size") int size,
                                              @Param("offset") int offset);
    
 // 더보기 버튼 표시 여부 판단을 위한 전체 예약 날짜 수 조회 (년/월 필터)
    @Select("SELECT COUNT(DISTINCT DATE(reserve_date)) " +
            "FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "WHERE p.user_id = #{sellerId} " +
            "AND r.status IN ('CONFIRMED', 'CANCELED') " +
            "AND YEAR(r.reserve_date) = #{year} " +
            "AND MONTH(r.reserve_date) = #{month}")
    int countDistinctDatesBySellerId(@Param("sellerId") Long sellerId,
                                      @Param("year") int year,
                                      @Param("month") int month);
}