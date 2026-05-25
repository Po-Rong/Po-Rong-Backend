package com.porong.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "email", "nickname", "role", "wishlistCount", "reviewCount"})
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String role;
    private int wishlistCount;
    private int reviewCount;
}
