package com.porong.backend.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.porong.backend.dto.request.RegisterRequestDto;
import com.porong.backend.mapper.UserMapper;
import com.porong.backend.vo.UserVO;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public ResponseEntity<?> register(RegisterRequestDto dto) {
		
		// 1. 이메일 중복 체크
		if (userMapper.existsByEmail(dto.getEmail()) > 0) {
			return ResponseEntity.status(409)
					.body(Map.of("message", "이미 사용중인 이메일입니다."));
		}
		
		// 2. 닉네임 중복 체크
		if (userMapper.existsByNickname(dto.getNickname()) > 0) {
			return ResponseEntity.status(409)
					.body(Map.of("message", "이미 사용중인 닉네임입니다."));
		}
		
		// 3. 비밀번호 규칙 체크
		if (!dto.getPassword().matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$")) {
			return ResponseEntity.status(400)
					.body(Map.of("message", "비밀번호는 영문, 숫자, 특수문자 조합 8자 이상이어야 합니다."));
		}
		
		// 4. VO에 담기
        UserVO user = new UserVO();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        
        // 5. DB 저장
        userMapper.insert(user);
        
        return ResponseEntity.status(201)
                .body(Map.of("message", "회원가입이 완료되었습니다."));
	}
}
