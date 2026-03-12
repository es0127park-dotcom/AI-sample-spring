<!-- Parent: ../AI-CONTEXT.md -->

# _core

## 목적

도메인에 의존하지 않는 전역 공통 기능 및 유틸리티를 관리합니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| utils/Resp.java | REST API를 위한 표준 응답 래퍼 (`Resp<T>`) |

## AI 작업 지침

- 비즈니스 로직을 포함하지 않으며, 전역적으로 재사용 가능한 코드만 위치함.
- `Resp.java`의 `ok`와 `fail` 메서드를 사용하여 일관된 API 응답을 유지할 것.

## 테스트

- 공통 유틸리티에 대한 JUnit 테스트.

## 의존성

- 내부: 없음 (최하위 유틸리티).
- 외부: Spring Framework (HttpStatus 등).

<!-- MANUAL -->
