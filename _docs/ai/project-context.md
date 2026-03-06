# PROJECT_CONTEXT.md

## 1. 프로젝트 개요
- **프로젝트 명**: Spring Boot Blog Project
- **목적**: 블로그 기능을 제공하는 웹 애플리케이션 (게시글 작성, 댓글 관리, 사용자 인증 등)
- **주요 기능**:
  - 게시글(Board): 목록, 상세, 작성, 수정, 삭제 (SSR 및 REST API 지원)
  - 댓글(Reply): 게시글별 댓글 작성 및 삭제
  - 사용자(User): 회원가입, 로그인, 정보 수정 (HttpSession 기반 인증)

## 2. 프로젝트 전체 아키텍처
- **도메인 기반 플랫 구조 (Domain-Driven Flat Structure)**:
  - 레이어 기반(Controller, Service, Repository 등)이 아닌, 도메인(Board, User, Reply) 단위로 모든 관련 파일을 한 폴더에 관리.
  - `_core/`: 도메인과 무관한 공통 유틸리티 및 전역 설정 관리.
- **SSR & REST API 분리**:
  - `{Domain}Controller.java`: Mustache 템플릿 엔진을 사용하는 서버 사이드 렌더링.
  - `{Domain}ApiController.java`: RESTful API 제공 (접두사 `/api` 필수).
- **데이터 흐름**:
  - `Entity` ↔ `Repository` ↔ `Service` ↔ `DTO` ↔ `Controller`
  - **규칙**: Entity는 Service 외부로 노출하지 않으며, Controller는 반드시 DTO를 통해 통신함.

## 3. 기술 스택
- **Language**: Java 21
- **Framework**: Spring Boot 3.x (Mustache Starter, Data JPA, Web MVC)
- **Database**: H2 (In-memory)
- **ORM**: Spring Data JPA (Hibernate)
- **Template Engine**: Mustache
- **Utility**: Lombok
- **Architecture**: Spring MVC + Domain-Driven Flat Layout

## 4. 핵심 개발 규칙
- **OSIV (Open Session In View)**: `false` (영속성 컨텍스트를 Service 레이어까지만 유지)
- **Fetch 전략**: 연관관계(`@ManyToOne` 등)는 반드시 `LAZY` 로딩 사용.
- **공통 응답**: 모든 REST API는 `_core/utils/Resp.java`를 사용한 공통 포맷(`Resp<T>`)으로 응답.
- **DTO 관리**: 도메인별 `{Domain}Request.java`, `{Domain}Response.java` 파일 내에 static inner class로 각 기능을 구분하여 정의.
- **Batch Size**: `default_batch_fetch_size=10` 설정을 통해 N+1 문제 최적화.

## 5. 데이터베이스 스키마 및 초기 데이터
- **초기화**: `src/main/resources/db/data.sql`을 통해 애플리케이션 시작 시 샘플 데이터 로드.
- **테이블 명**: `{domain}_tb` 형식 사용 (예: `board_tb`, `user_tb`, `reply_tb`).
