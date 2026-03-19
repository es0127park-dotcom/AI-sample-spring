# 🚩 작업 보고서: 회원 탈퇴 및 마이페이지 화면 구현 (T-2.3)

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

```
[사용자] 로그인 상태에서 헤더의 "회원정보수정" 클릭
         │
         ▼
[GET /user/update-form]
         │
         ├─ 세션 없음? → redirect:/login-form
         │
         ├─ 세션 있음 → UserService.findById(id)
         │                    │
         │                    ▼
         │              UserResponse.Max DTO 생성
         │                    │
         │                    ▼
         │              model에 "user" 담아 update-form.mustache 렌더링
         │
         ▼
┌─────────────────────────────────────────┐
│            회원 정보 수정               │
│─────────────────────────────────────────│
│  아이디: [ ssar         ] (readonly)    │
│  새 비밀번호: [ ●●●●●●  ]              │
│  이메일: [ ssar@nate.com ]              │
│  ─────────────────────────────────────  │
│  우편번호: [12345] [우편번호 찾기]      │
│  주소: [ 서울시 강남구 ... ]            │
│  상세주소: [  ]  참고항목: [  ]         │
│         [ 수정하기 ]                    │
│  ─────────────────────────────────────  │
│  더 이상 서비스를 이용하지 않으시겠어요?│
│         [ 회원 탈퇴 ]                   │
└─────────────────────────────────────────┘
         │                    │
    [수정하기]           [회원 탈퇴]
         │                    │
         ▼                    ▼
[POST /user/update]    모달 확인 다이얼로그
         │                    │
         ▼                    ▼
UserService.update()   [POST /user/delete]
  ├─ BCrypt 암호화           │
  ├─ 더티체킹 자동 UPDATE    ▼
  ├─ 세션 갱신          UserService.delete()
  └─ redirect 수정폼      ├─ boardRepository.deleteByUserId()
                           ├─ userRepository.deleteById()
                           ├─ session.invalidate()
                           └─ redirect:/
```

## 2. 🧩 변경된 모든 코드 포함

### 2-1. UserRequest.java — Update DTO 추가

```java
@Data
public static class Update {
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 4, max = 20, message = "비밀번호는 4자에서 20자 사이여야 합니다")
    private String password;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    // 주소 필드 (선택)
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
}
```

### 2-2. UserResponse.java — Max DTO 추가

```java
@Data
public static class Max {
    private Integer id;
    private String username;
    private String email;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

    // Entity → DTO 변환 생성자
    public Max(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.postcode = user.getPostcode();
        this.address = user.getAddress();
        this.detailAddress = user.getDetailAddress();
        this.extraAddress = user.getExtraAddress();
    }
}
```

### 2-3. UserService.java — findById, update, delete 추가

```java
// 회원 정보 조회 (수정 폼용) — readOnly 트랜잭션
public UserResponse.Max findById(Integer id) {
    var user = userRepository.findById(id)
            .orElseThrow(() -> new Exception400("존재하지 않는 회원입니다."));
    return new UserResponse.Max(user); // Entity → DTO 변환
}

// 회원 정보 수정 — 더티체킹으로 자동 UPDATE
@Transactional
public User update(Integer id, UserRequest.Update reqDTO) {
    var user = userRepository.findById(id)
            .orElseThrow(() -> new Exception400("존재하지 않는 회원입니다."));

    // BCrypt로 새 비밀번호 암호화 후 setter로 변경
    user.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
    user.setEmail(reqDTO.getEmail());
    user.setPostcode(reqDTO.getPostcode());
    user.setAddress(reqDTO.getAddress());
    user.setDetailAddress(reqDTO.getDetailAddress());
    user.setExtraAddress(reqDTO.getExtraAddress());

    return user; // @Transactional 끝날 때 더티체킹 → UPDATE 쿼리 자동 실행
}

// 회원 탈퇴 — 게시글 먼저 삭제 후 유저 삭제 (Cascade Delete)
@Transactional
public void delete(Integer id) {
    boardRepository.deleteByUserId(id); // FK 제약조건 위반 방지: 자식 먼저 삭제
    userRepository.deleteById(id);       // 부모(유저) 삭제
}
```

### 2-4. UserController.java — 수정/탈퇴 엔드포인트 추가

```java
// 회원 정보 수정 페이지 반환 — 세션 체크 후 DB에서 최신 정보 조회
@GetMapping("/user/update-form")
public String updateForm(Model model) {
    var sessionUser = (User) session.getAttribute("sessionUser");
    if (sessionUser == null) {
        return "redirect:/login-form"; // 미인증 사용자 차단
    }
    var user = userService.findById(sessionUser.getId());
    model.addAttribute("user", user); // Mustache에서 {{user.email}} 등으로 접근
    return "user/update-form";
}

// 회원 정보 수정 처리 — 수정 후 세션도 갱신
@PostMapping("/user/update")
public String update(@Valid UserRequest.Update reqDTO, BindingResult bindingResult) {
    var sessionUser = (User) session.getAttribute("sessionUser");
    if (sessionUser == null) {
        return "redirect:/login-form";
    }
    var updatedUser = userService.update(sessionUser.getId(), reqDTO);
    session.setAttribute("sessionUser", updatedUser); // 세션에 최신 유저 정보 반영
    return "redirect:/user/update-form";
}

// 회원 탈퇴 처리 — 삭제 후 세션 무효화
@PostMapping("/user/delete")
public String delete() {
    var sessionUser = (User) session.getAttribute("sessionUser");
    if (sessionUser == null) {
        return "redirect:/login-form";
    }
    userService.delete(sessionUser.getId());
    session.invalidate(); // 세션 파기 → 로그아웃 처리
    return "redirect:/";
}
```

### 2-5. BoardRepository.java — 유저별 게시글 삭제 쿼리

```java
// JPQL로 특정 유저의 게시글 일괄 삭제
@Modifying  // DELETE/UPDATE 쿼리에 필수
@Query("delete from Board b where b.user.id = :userId")
void deleteByUserId(@Param("userId") Integer userId);
```

### 2-6. update-form.mustache — 회원 정보 수정 화면

join-form과 동일한 디자인 시스템 적용. 주요 특징:
- 아이디 필드 `readonly`로 변경 불가
- 기존 값 `{{user.email}}`, `{{user.address}}` 등으로 프리필
- 다음 우편번호 API 연동
- 탈퇴 버튼 클릭 시 Bootstrap 모달로 2차 확인

## 3. 🍦 상세비유 쉬운 예시를 들어서 (Easy Analogy)

이번 작업은 **"은행 창구에서 개인정보를 수정하고, 계좌를 해지하는 것"**과 같습니다.

- **회원 정보 수정** = 은행에 가서 신분증(세션)을 보여주고, 주소나 비밀번호를 바꾸는 것. 은행 직원(Service)이 원장(DB)을 직접 수정해주고, 통장(세션)에도 반영해줍니다.
- **회원 탈퇴** = 계좌 해지 전에 그 계좌로 보낸 이체 내역(게시글)을 먼저 정리해야 합니다. 이체 기록이 남아있는데 계좌를 지우면 연결이 끊어져 오류가 나니까요. 그래서 "자식(게시글) 먼저 삭제 → 부모(유저) 삭제" 순서를 지킵니다.
- **모달 확인** = 은행 창구에서 "정말 해지하시겠습니까?" 한 번 더 물어보는 것. 실수로 탈퇴 버튼을 누르는 사고를 방지합니다.

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

### **더티 체킹 (Dirty Checking)**
JPA의 영속성 컨텍스트는 엔티티의 변경을 자동 감지합니다. `@Transactional` 메서드 안에서 엔티티의 setter를 호출하면, 트랜잭션이 끝나는 시점에 변경된 필드만 UPDATE SQL이 자동 실행됩니다.

```java
@Transactional
public User update(Integer id, UserRequest.Update reqDTO) {
    var user = userRepository.findById(id).orElseThrow(...);
    user.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
    // save() 호출 없이도 트랜잭션 종료 시 자동 UPDATE!
    return user;
}
```

### **@Modifying + JPQL DELETE**
Spring Data JPA에서 `@Query`로 DELETE/UPDATE를 실행하려면 반드시 `@Modifying`을 붙여야 합니다. 이 어노테이션이 없으면 Spring은 해당 쿼리를 SELECT로 인식하여 오류가 발생합니다.

```java
@Modifying
@Query("delete from Board b where b.user.id = :userId")
void deleteByUserId(@Param("userId") Integer userId);
```

### **FK 제약조건과 삭제 순서**
`board_tb.user_id`가 `user_tb.id`를 참조하는 외래 키(FK)이므로, 유저를 먼저 삭제하면 `ConstraintViolationException`이 발생합니다. 반드시 자식 테이블(board) → 부모 테이블(user) 순서로 삭제해야 합니다.

### **세션 갱신**
수정 후 `session.setAttribute("sessionUser", updatedUser)`로 세션을 갱신하지 않으면, 헤더 네비게이션 등에서 이전 정보가 계속 표시됩니다. 수정된 엔티티를 세션에 다시 넣어 동기화합니다.
