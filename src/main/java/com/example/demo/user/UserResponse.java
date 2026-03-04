package com.example.demo.user;

import lombok.Data;

/**
 * RULE : Max, Min, Detail - DTO 이름을 최대, 최소, 상세를 기본으로 한다
 */
public class UserResponse {

    @Data
    public static class Min {
        private String username;
        private String email;

        public Min(User user) {
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }

}
