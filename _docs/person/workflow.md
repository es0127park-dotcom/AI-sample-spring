# 아이디(username) 중복체크 기능

## 개요

회원가입 시 username 입력 후 중복 여부를 Ajax로 확인하는 REST API

## 흐름

```
[사용자] → username 입력 → "중복확인" 버튼 클릭
   ↓
[JS/fetch] → GET /api/users/username-check?username=xxx
   ↓
[UserApiController] → userService.usernameCheck(username) 호출
   ↓
[UserService] → userRepository.findByUsername(username)
   ↓  존재하면 → 중복 (사용 불가)
   ↓  없으면   → 사용 가능
   ↓
[응답] → Resp.ok(true/false) 또는 Resp.fail(...)
```

## 작업 목록

### 1. UserApiController 생성

- 파일: `user/UserApiController.java`
- `@RequiredArgsConstructor` → `@RestController`
- 엔드포인트: `GET /api/users/username-check?username=xxx`
- 응답: `Resp.ok()`

### 2. UserService에 메서드 추가

- 메서드: `usernameCheck(String username)`
- `findByUsername()`으로 조회 → 존재 여부 반환
- 읽기 전용이므로 `@Transactional` 별도 선언 불필요 (클래스 레벨 readOnly 상속)

### 3. UserRepository

- `findByUsername(String username)` — 이미 존재함, 추가 작업 없음

### 4. 프론트 (Mustache + JS)

- 회원가입 페이지에서 username 입력란 옆 "중복확인" 버튼
- fetch로 `/api/users/username-check?username=xxx` 호출
- 응답에 따라 "사용 가능" / "이미 사용중" 메시지 표시
