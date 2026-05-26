package com.porong.backend.mapper;

import com.porong.backend.dto.request.ReservationCreateRequestDto;
import com.porong.backend.dto.request.ReservationUpdateRequestDto;
import com.porong.backend.dto.response.ReservationResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReservationMapper {

    // 1. 팝업 예약 신청
    @Insert("INSERT INTO reservations (popup_id, user_id, reserve_date, user_name, user_phone, status, created_at) " +
            "VALUES (#{popupId}, #{req.userId}, #{req.reserveDate}, #{req.userName}, #{req.userPhone}, 'CONFIRMED', NOW())")
    int insertReservation(@Param("popupId") Long popupId, @Param("req") ReservationCreateRequestDto request);

    // 2. 내 예약 내역 조회
    @Select("SELECT r.id, " +
            "       r.user_id AS userId, " + 
            "       r.popup_id AS popupId, " +
            "       p.title AS popupTitle, " +
            "       p.main_image_url AS mainImageUrl, " +
            "       r.reserve_date AS reserveDate, " +
            "       r.status, " +
            "       r.user_name AS userName, " +
            "       r.user_phone AS userPhone, " +
            "       r.created_at AS createdAt " +
            "FROM reservations r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "WHERE r.user_id = #{userId} " +
            "ORDER BY r.created_at DESC")
    List<ReservationResponseDto> selectMyReservations(@Param("userId") Long userId);

    // 본인 검증 및 확인을 위한 단건 조회 쿼리
    @Select("SELECT id, user_id AS userId, status FROM reservations WHERE id = #{reservationId}")
    ReservationResponseDto selectReservationById(@Param("reservationId") Long reservationId);

    // 3. 팝업 예약 취소 (상태값만 CANCELED로 변경)
    @Update("UPDATE reservations SET status = 'CANCELED' WHERE id = #{reservationId}")
    int cancelReservation(@Param("reservationId") Long reservationId);

    // 4. 팝업 예약 정보 수정
    @Update("UPDATE reservations SET reserve_date = #{req.reserveDate}, user_name = #{req.userName}, user_phone = #{req.userPhone} " +
            "WHERE id = #{reservationId}")
    int updateReservation(@Param("reservationId") Long reservationId, @Param("req") ReservationUpdateRequestDto request);
}