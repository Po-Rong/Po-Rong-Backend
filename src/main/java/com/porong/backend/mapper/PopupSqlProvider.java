package com.porong.backend.mapper;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class PopupSqlProvider {

    public String getPopupList(Map<String, Object> params) {
        String sql =  new SQL() {{
            SELECT("p.id, p.title, p.main_image_url, p.start_date, p.end_date, " +
                   "p.reservation_start_date, p.reservation_end_date, " +
                   "c.category_name, r.region_name, " +
                   "CASE WHEN NOW() < p.start_date THEN 'upcoming' " +
                   "     WHEN NOW() > p.end_date THEN 'closed' " +
                   "     ELSE 'ongoing' END AS status, " +
                   "ROUND(COALESCE(AVG(rv.rating), 0), 1) AS avg_rating, " +
                   // 혼잡도 정렬일 때 점수화 평균 계산 필드 주입
                   "ROUND(COALESCE(AVG(CASE WHEN rv.congestion_level = 'HIGH' THEN 3 " +
                   "                        WHEN rv.congestion_level = 'NORMAL' THEN 2 " +
                   "                        ELSE 1 END), 0), 1) AS avg_congestion_score, " +
                   (params.get("userId") != null ?
                   "(SELECT COUNT(*) > 0 FROM wishlists w WHERE w.user_id = " + params.get("userId") + " AND w.popup_id = p.id) AS is_wishlisted" :
                   "FALSE AS is_wishlisted"));
            FROM("popups p");
            LEFT_OUTER_JOIN("categories c ON p.category_id = c.id");
            LEFT_OUTER_JOIN("regions r ON p.region_id = r.id");
            LEFT_OUTER_JOIN("reviews rv ON p.id = rv.popup_id");
            if (params.get("sellerId") != null) {
                WHERE("p.user_id = #{sellerId}");
            }
            if (params.get("regionId") != null) {
                WHERE("p.region_id = #{regionId}");
            }
            if (params.get("categoryId") != null) {
                WHERE("p.category_id = #{categoryId}");
            }
            if (params.get("status") != null) {
                if (params.get("status").equals("upcoming")) {
                    WHERE("NOW() < p.start_date");
                } else if (params.get("status").equals("ongoing")) {
                    WHERE("NOW() BETWEEN p.start_date AND p.end_date");
                } else if (params.get("status").equals("closed")) {
                    WHERE("NOW() > p.end_date");
                }
            }
            if (params.get("keyword") != null) {
                WHERE("p.title LIKE CONCAT('%', #{keyword}, '%')");
            }
            GROUP_BY("p.id");
            if (params.get("sort") != null) {
                if (params.get("sort").equals("end_date")) {
                    ORDER_BY("p.end_date ASC");
                } else if (params.get("sort").equals("rating")) {
                    ORDER_BY("avg_rating DESC");
                } else if (params.get("sort").equals("wishlist")) {
                    ORDER_BY("(SELECT COUNT(*) FROM wishlists w WHERE w.popup_id = p.id) DESC");
                } else if (params.get("sort").equals("leisurely")) {
                    // 혼잡도 평균이 낮은 순(여유로운 순)
                    ORDER_BY("avg_congestion_score ASC, COUNT(rv.id) DESC");
                } else {
                    ORDER_BY("p.id DESC");
                }
            } else {
                ORDER_BY("p.id DESC");
            }
        }}.toString();
        
        // 혼잡도 조건일 때는 TOP 10만 메인에 띄워야 하므로 LIMIT 구문 결합
        if (params.get("sort") != null && params.get("sort").equals("leisurely")) {
            sql += " LIMIT 10";
        }
        
        return sql;
    }
}