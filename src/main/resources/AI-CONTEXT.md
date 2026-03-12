<!-- Parent: ../AI-CONTEXT.md -->

# resources

## 목적

애플리케이션 설정, 정적 리소스, 뷰 템플릿 등을 보관합니다.

## 하위 디렉토리

- `db/` - 데이터베이스 스키마 및 초기 데이터 초기화 (`data.sql`)
- `templates/` - SSR을 위한 Mustache 템플릿 파일
- `static/` - CSS, JS 등 정적 자산

## AI 작업 지침

- `application.properties`에서 OSIV 설정이 `false`인지 항상 확인할 것.
- `data.sql`은 애플리케이션 시작 시 데이터 초기화에 사용됨.

## 테스트

- 리소스 파일 자체에 대한 테스트는 수행하지 않으나, 템플릿 렌더링 결과 확인을 위한 통합 테스트에 사용됨.

## 의존성

- 외부: Spring Boot Resource Loader.

<!-- MANUAL -->
