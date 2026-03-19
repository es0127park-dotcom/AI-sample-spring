package com.example.demo.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._core.handler.ex.Exception400;
import com.example.demo.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 (비밀번호 암호화 후 저장)
    @Transactional
    public void join(UserRequest.Join reqDTO) {
        // 아이디 중복 체크
        var userOp = userRepository.findByUsername(reqDTO.getUsername());
        if (userOp.isPresent()) {
            throw new Exception400("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화 (BCrypt)
        var encPassword = passwordEncoder.encode(reqDTO.getPassword());
        reqDTO.setPassword(encPassword);

        // 엔티티 변환 후 저장
        userRepository.save(reqDTO.toEntity());
    }

    // 아이디 중복 체크 (true: 사용 가능, false: 중복)
    public boolean usernameSameCheck(String username) {
        var userOp = userRepository.findByUsername(username);
        return userOp.isEmpty();
    }

    // 로그인 (아이디 확인 및 비밀번호 매칭)
    public User login(UserRequest.Login reqDTO) {
        var userOp = userRepository.findByUsername(reqDTO.getUsername());
        
        // 아이디 존재 확인
        if (userOp.isEmpty()) {
            throw new Exception400("아이디 혹은 비밀번호가 틀렸습니다.");
        }

        var user = userOp.get();

        // 비밀번호 매칭 확인
        if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
            throw new Exception400("아이디 혹은 비밀번호가 틀렸습니다.");
        }

        return user;
    }

    // 회원 정보 조회 (수정 폼용)
    public UserResponse.Max findById(Integer id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("존재하지 않는 회원입니다."));
        return new UserResponse.Max(user);
    }

    // 회원 정보 수정
    @Transactional
    public User update(Integer id, UserRequest.Update reqDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("존재하지 않는 회원입니다."));

        user.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
        user.setEmail(reqDTO.getEmail());
        user.setPostcode(reqDTO.getPostcode());
        user.setAddress(reqDTO.getAddress());
        user.setDetailAddress(reqDTO.getDetailAddress());
        user.setExtraAddress(reqDTO.getExtraAddress());

        return user;
    }

    // 회원 탈퇴 (게시글 먼저 삭제 후 유저 삭제)
    @Transactional
    public void delete(Integer id) {
        boardRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
