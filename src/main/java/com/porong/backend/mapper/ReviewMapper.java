package com.porong.backend.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.porong.backend.dto.response.AdminReviewResponseDto;
import com.porong.backend.dto.response.ReviewListResponseDto;
import com.porong.backend.vo.ReviewVO;

@Mapper
public interface ReviewMapper {
	// 특정 팝업의 리뷰 목록 전체 조회
	@Select("""
	        <script>
	        SELECT 
	            r.id AS reviewId,
	            r.user_id AS userId,
	            u.nickname,
	            DATE_FORMAT(res.reserve_date, '%Y-%m-%d') AS reserveDate,
	            CASE 
	                WHEN DATE_FORMAT(res.reserve_date, '%H') &lt; 12 THEN DATE_FORMAT(res.reserve_date, '오전 %h시')
	                ELSE DATE_FORMAT(res.reserve_date, '오후 %h시')
	            END AS reserveTime,
	            r.congestion_level,
	            r.content,
	            r.review_image_url,
	            r.rating,
	            r.reservation_id AS reservationId -- 📍 [추가] DB의 예약 ID를 응답 필드와 매핑
	        FROM reviews r
	        INNER JOIN users u ON r.user_id = u.id
	        INNER JOIN reservations res ON r.reservation_id = res.id 
	        WHERE r.popup_id = #{popupId}
	        <choose>
	            <when test="sort == 'rating_high'">
	                ORDER BY r.rating DESC, r.created_at DESC
	            </when>
	            <when test="sort == 'rating_low'">
	                ORDER BY r.rating ASC, r.created_at DESC
	            </when>
	            <otherwise>
	                ORDER BY r.created_at DESC
	            </otherwise>
	        </choose>
	        </script>
	    """)
	    List<ReviewListResponseDto> selectReviewsByPopupId(@Param("popupId") Long popupId, @Param("sort") String sort);

    // 리뷰 단건 조회
	@Select("SELECT id, content, rating, congestion_level, review_image_url, popup_id, user_id, created_at "
		      + "FROM reviews WHERE id = #{id}")
	ReviewVO findById(Long id);
	
	@Select("""
	        SELECT 
	            r.id AS reviewId, 
	            r.content, 
	            r.rating, 
	            r.congestion_level AS congestionLevel, 
	            r.review_image_url AS reviewImageUrl, 
	            r.popup_id AS popupId, 
	            r.user_id AS userId, 
	            DATE_FORMAT(r.created_at, '%Y-%m-%d %H:%i:%s') AS createdAt,
	            p.main_image_url AS popupMainImageUrl
	        FROM reviews r
	        INNER JOIN popups p ON r.popup_id = p.id
	        WHERE r.id = #{id}
	    """)
	    Map<String, Object> findReviewDetailById(@Param("id") Long id);

    // 리뷰 등록
	@Insert("""
	        INSERT INTO reviews (content, rating, congestion_level, review_image_url, popup_id, user_id, reservation_id) 
	        VALUES (#{content}, #{rating}, #{congestionLevel}, #{reviewImageUrl}, #{popupId}, #{userId}, #{reservationId})
	    """)
	    @Options(useGeneratedKeys = true, keyProperty = "id")
	    int insertReview(ReviewVO review);

    // 리뷰 수정
    @Update("UPDATE reviews SET content = #{content}, rating = #{rating}, "
          + "congestion_level = #{congestionLevel}, review_image_url = #{reviewImageUrl} "
          + "WHERE id = #{id}")
    int updateReview(ReviewVO review);

    // 리뷰 삭제
    @Delete("DELETE FROM reviews WHERE id = #{id}")
    int deleteReview(Long id);

    // 유저가 여태까지 작성한 총 리뷰 개수 구하기
    @Select("SELECT COUNT(*) FROM reviews WHERE user_id = #{userId}")
    int countReviewsByUserId(Long userId);

    // 리뷰 개수 조건(1~5)에 딱 맞는 캐릭터 키링 ID 및 이름 조회하기
    @Select("SELECT id, name FROM keyrings WHERE review_count = #{reviewCount} LIMIT 1")
    java.util.Map<String, Object> findKeyringByReviewCount(int reviewCount);

    // 유저 도감(collection_books)에 획득한 키링 추가하기
    @Insert("""
            INSERT INTO collection_books (user_id, keyring_id, popup_id) 
            VALUES (#{userId}, #{keyringId}, #{popupId})
        """)
        int insertCollectionBook(@Param("userId") Long userId, @Param("keyringId") Long keyringId, @Param("popupId") Long popupId);

    // 리뷰 삭제 시 연동된 유저 도감 키링 회수 (삭제)
    @Delete("""
    		DELETE FROM collection_books 
    		WHERE user_id = #{userId} AND popup_id = #{popupId}
    		""")
        int deleteCollectionBook(@Param("userId") Long userId, @Param("popupId") Long popupId);
    
    // 판매자 특정 팝업 리뷰 조회
    @Select("SELECT * FROM reviews WHERE popup_id = #{popupId}")
    List<ReviewVO> findByPopupId(Long popupId);

    // 판매자 전체 팝업 리뷰 조회
    @Select("SELECT r.*, u.nickname, p.title as popupTitle, " +
            "p.main_image_url as popupMainImageUrl, " +
            "CASE " +
            "  WHEN NOW() < p.start_date THEN 'upcoming' " +
            "  WHEN NOW() > p.end_date THEN 'closed' " +
            "  ELSE 'ongoing' " +
            "END as popupStatus, " +
            "c.category_name as categoryName, rg.region_name as regionName " +
            "FROM reviews r " +
            "JOIN popups p ON r.popup_id = p.id " +
            "JOIN users u ON r.user_id = u.id " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN regions rg ON p.region_id = rg.id " +
            "WHERE p.user_id = #{sellerId}")
    List<AdminReviewResponseDto> findAllBySellerId(Long sellerId);
    
    @Select("""
    	    SELECT COUNT(*) > 0 
    	    FROM collection_books 
    	    WHERE user_id = #{userId} AND popup_id = #{popupId}
    	""")
    	boolean existsCollectionBook(@Param("userId") Long userId, @Param("popupId") Long popupId);
    
//    최근 리뷰 3건 조회
    @Select("""
            SELECT 
                r.id AS reviewId,
                r.user_id AS userId,
                u.nickname,
                p.id AS popupId,
                p.title AS popupTitle,
                p.main_image_url AS popupMainImageUrl,
                r.content,
                r.rating,
                r.congestion_level AS congestionLevel,
                r.review_image_url AS reviewImageUrl,
                DATE_FORMAT(r.created_at, '%Y-%m-%d %H:%i:%s') AS createdAt,
                DATE_FORMAT(res.reserve_date, '%Y-%m-%d') AS reserveDate,
                CASE 
                    WHEN DATE_FORMAT(res.reserve_date, '%H') < 12 THEN DATE_FORMAT(res.reserve_date, '오전 %h시')
                    ELSE DATE_FORMAT(res.reserve_date, '오후 %h시')
                END AS reserveTime
            FROM reviews r
            INNER JOIN users u ON r.user_id = u.id
            INNER JOIN popups p ON r.popup_id = p.id
            INNER JOIN reservations res ON r.reservation_id = res.id
            ORDER BY res.reserve_date DESC
            LIMIT 3
        """)
        List<Map<String, Object>> selectRecentThreeReviews();
}
