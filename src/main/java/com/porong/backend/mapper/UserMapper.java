package com.porong.backend.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.porong.backend.vo.UserVO;

@Mapper
public interface UserMapper {
	
//	회원 가입 INSERT
//	id, role, created_at 은 DB에서 자동 생성되므로 쿼리에서 제외
	@Insert("INSERT INTO users(email, password, nickname, role) "
			+ "VALUES (#{email},#{password},#{nickname}, #{role})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(UserVO user);
	
    // 이메일 중복 체크
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int existsByEmail(String email);

    // 닉네임 중복 체크
    @Select("SELECT COUNT(*) FROM users WHERE nickname = #{nickname}")
    int existsByNickname(String nickname);
    
    // 이메일로 유저 조회
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserVO findByEmail(String email);
    
    // 찜 수 조회
    @Select("SELECT COUNT(*) FROM wishlists WHERE user_id = #{userId}")
    int countWishlist(Long userId);

    // 리뷰 수 조회
    @Select("SELECT COUNT(*) FROM reviews WHERE user_id = #{userId}")
    int countReview(Long userId);
    
    // ID로 유저 조회
    @Select("SELECT * FROM users WHERE id = #{id}")
    UserVO findById(Long id);
    
    // 닉네임 수정
    @Update("UPDATE users SET nickname = #{nickname} WHERE id = #{id}")
    int updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
    
    // 회원 탈퇴
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteUser(Long id);
}
