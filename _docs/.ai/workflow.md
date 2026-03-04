# Development Workflows

이 문서는 프로젝트의 주요 작업 절차(Workflow)를 정의하여 AI와 인간의 협업 효율을 높입니다.

## 1. NEW_FEATURE_IMPLEMENTATION (기능 추가)
1.  **Domain Analysis**: 도메인 모델과 필요 필드 분석.
2.  **Entity Creation**: `xxx_tb` 규칙에 맞춰 Entity 생성 (Repository 포함).
3.  **DTO Definition**: `Request`/`Response` 클래스 내부에 static inner class로 필요한 데이터 구조 정의.
4.  **Service Implementation**:
    - `@Transactional(readOnly = true)` 설정.
    - 비즈니스 로직 구현.
    - Entity를 DTO로 변환하여 반환.
5.  **Controller Mapping**: 뷰 이름 반환 및 서비스 호출.
6.  **UI/Template**: Mustache 템플릿 작성 및 데이터 바인딩 확인.

## 2. REFACTORING_WORKFLOW (리팩토링)
1.  **Convention Audit**: `code-rule.md`와 비교하여 위반 사항 식별.
2.  **Surgical Fix**: 비즈니스 로직은 유지하되 구조(DTO 변환 위치, 어노테이션 등)만 수정.
3.  **Verification**: 기존 기능의 정상 작동 확인.

## 3. BUG_FIX_WORKFLOW (버그 수정)
1.  **Issue Identification**: 로그나 현상을 분석하여 원인 파악.
2.  **Root Cause Analysis**: 특히 OSIV false로 인한 LazyInitializationException 등을 중점적으로 체크.
3.  **Fix & Validate**: 트랜잭션 범위 조정 또는 Fetch Join 등을 활용하여 해결.
