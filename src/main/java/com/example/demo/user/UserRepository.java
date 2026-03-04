package com.example.demo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// 이렇게 하면 @Repository 안 붙여도 됨
public interface UserRepository extends JpaRepository<User, Integer> {

    // 네임 쿼리 (findByUsername - 유저네임으로 찾기 / findByEmail - 이메일로 찾기)
    Optional<User> findByUsername(String username);
}
