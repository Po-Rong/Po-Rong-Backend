package com.porong.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.porong.backend.vo.UserVO;

@Mapper
public interface UserMapper {
	
//	회원 가입 INSERT
//	id, role, created_at 은 DB에서 자동 생성되므로 쿼리에서 제외
	@Insert("INSERT INTO users(email, password, nickname) "
			+ "VALUES (#{email},#{password},#{nickname})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(UserVO user);
	
    // 이메일 중복 체크
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int existsByEmail(String email);

    // 닉네임 중복 체크
    @Select("SELECT COUNT(*) FROM users WHERE nickname = #{nickname}")
    int existsByNickname(String nickname);
}
