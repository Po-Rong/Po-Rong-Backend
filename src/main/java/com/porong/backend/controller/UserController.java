package com.porong.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porong.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// 닉네임 중복 확인
	@GetMapping("/check-nickname")
	public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
	    return userService.checkNickname(nickname);
	}
}
