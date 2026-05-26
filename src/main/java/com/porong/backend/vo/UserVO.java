package com.porong.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
	
	private Long id;
	private String email;
	private String nickname;
	private String password;
	private String role; // 권한 (user = 일반회원, seller = 판매자)
	private String createdAt;
	
}
