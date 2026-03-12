<!-- Parent: ../AI-CONTEXT.md -->

# templates

## 목적

서버 사이드 렌더링(SSR)을 위한 Mustache 템플릿 파일들을 관리합니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| home.mustache | 메인 홈 화면 |

## 하위 디렉토리

- `layout/` - 공통 헤더/푸터 레이아웃
- `user/` - 사용자 관련 뷰 (회원가입, 로그인 등)
- `board/` - 게시판 관련 뷰 (목록, 상세, 작성 등 - 현재 board 폴더 확인 필요)

## AI 작업 지침

- Mustache 문법을 사용하여 서버 데이터를 바인딩할 것.
- 레이아웃 재사용을 위해 `{{> layout/header}}` 형식을 적극 활용할 것.
- `common-rule.md`에 따라 폼 데이터 제출은 기본적으로 `<form>` 태그 방식을 사용하고, 필요 시 fetch를 사용할 것.

## 테스트

- 컨트롤러 통합 테스트(`MockMvc`)를 통해 뷰 렌더링 결과 검증.

## 의존성

- 외부: Spring Boot Starter Mustache.

<!-- MANUAL -->
