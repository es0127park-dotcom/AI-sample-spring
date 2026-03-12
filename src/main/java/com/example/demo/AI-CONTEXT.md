<!-- Parent: ../../../AI-CONTEXT.md -->

# com.example.demo

## 목적

프로젝트의 메인 도메인 로직이 위치하는 최상위 패키지입니다. 도메인 기반 플랫 구조를 따릅니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| DemoApplication.java | Spring Boot 애플리케이션 시작점 |

## 하위 디렉토리

- `_core/` - 전역 공통 유틸리티 (응답 처리 등)
- `board/` - 게시판 도메인 (게시글 조회, 작성, 수정, 삭제)
- `user/` - 회원 도메인 (회원가입, 로그인, 정보수정)
- `reply/` - 댓글 도메인 (댓글 작성, 삭제)

## AI 작업 지침

- 도메인 폴더 내에는 Controller, Service, Repository, DTO, Entity가 수평적으로 위치해야 함.
- 레이어별(controller/, service/) 폴더를 추가로 생성하지 말 것.
- `common-rule.md`에 정의된 명명 규칙과 어노테이션 순서를 엄격히 준수할 것.

## 테스트

- 각 도메인별 단위 테스트는 `src/test/java/com/example/demo/` 하위에 도메인 구조를 따라 위치함.

## 의존성

- 내부: `_core/` 패키지에 정의된 유틸리티를 공통으로 참조.
- 외부: Spring Boot 관련 라이브러리.

<!-- MANUAL -->
