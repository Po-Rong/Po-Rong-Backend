package com.porong.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CongestionMapper {

    // 1. 팝업별 혼잡도 점수화 평균 계산 (HIGH=3, NORMAL=2, LOW=1)
    @Select("SELECT AVG(CASE WHEN congestion_level = 'HIGH' THEN 3 " +
            "                WHEN congestion_level = 'NORMAL' THEN 2 " +
            "                ELSE 1 END) " +
            "FROM reviews WHERE popup_id = #{popupId}")
    Double selectAverageCongestionScore(@Param("popupId") Long popupId);

    // 2. 통계에 반영된 해당 팝업의 총 리뷰 개수 계산
    @Select("SELECT COUNT(*) FROM reviews WHERE popup_id = #{popupId}")
    int selectTotalReportCount(@Param("popupId") Long popupId);
}