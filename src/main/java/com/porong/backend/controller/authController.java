package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.dto.request.LoginRequestDto;
import com.porong.backend.dto.request.RegisterRequestDto;
import com.porong.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class authController {
	
	@Autowired
	private UserService userService;
	
	// 회원 가입
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto dto) {
		return userService.register(dto);
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
	    return userService.login(dto);
	}
	
}
