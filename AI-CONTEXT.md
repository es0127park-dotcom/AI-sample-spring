# Demo Project Root

## 목적

Spring Boot 기반의 도메인 기반 플랫 구조 연습용 프로젝트입니다. SSR(Mustache)과 REST API를 모두 지원하며, OSIV 비활성화 및 지연 로딩을 기본으로 합니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| build.gradle | 프로젝트 의존성 및 설정 |
| AI-GUIDE.md | AI를 위한 개발 가이드 및 스킬 정의 |
| .ai/rules/common-rule.md | 프로젝트 공통 코드 컨벤션 |
| gradlew | Gradle 실행 래퍼 (Unix/macOS) |
| gradlew.bat | Gradle 실행 래퍼 (Windows) |

## 하위 디렉토리

- `src/main/java/com/example/demo/` - 메인 소스 코드 (도메인 기반 플랫 구조)
- `src/main/resources/` - 설정 및 템플릿, 데이터 초기화 SQL
- `.ai/` - AI 전용 규칙 및 스킬 정의
- `.person/` - 개인화된 워크플로우 및 작업 정의

## AI 작업 지침

- 새로운 도메인 추가 시 `common-rule.md`의 패키지 구조를 준수할 것.
- 모든 REST API는 `_core/utils/Resp.java`를 통한 공통 응답 형식을 따를 것.
- OSIV가 `false`이므로 Service 레이어에서 DTO 변환을 완료해야 함.

## 테스트

- `./gradlew test` 실행을 통해 전체 테스트 수행 가능.

## 의존성

- 내부: 도메인 간 참조는 최소화하며, Service를 통해 상호작용.
- 외부: Spring Boot Starter Data JPA, Web, Mustache, H2 Database, Lombok.

<!-- MANUAL -->
<!-- 이 아래는 수동으로 추가된 내용을 보존합니다. -->
