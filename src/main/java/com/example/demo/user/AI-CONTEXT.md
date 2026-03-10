<!-- Parent: ../AI-CONTEXT.md -->

# user

## 목적

회원 관리 및 인증 관련 로직을 담당하는 도메인입니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| User.java | 회원 엔티티 (`user_tb`) |
| UserController.java | 회원가입, 로그인 페이지 제공 컨트롤러 |
| UserService.java | 회원가입, 로그인 비즈니스 로직 |
| UserRepository.java | 회원 데이터 접근 인터페이스 |
| UserRequest.java | `Join`, `Login` 등 요청 DTO |
| UserResponse.java | 정보 조회용 응답 DTO |

## AI 작업 지침

- 비밀번호 등 민감 정보는 엔티티에서 `@Column` 설정을 통해 노출을 제어하거나 DTO 변환 시 제외할 것.
- `HttpSession`을 주입받아 인증 상태를 관리할 것 (Security 사용 전까지).
