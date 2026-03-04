# AI Skills for Spring Boot Development

이 문서는 AI가 프로젝트에서 수행할 수 있는 구체적인 기술적 역량을 정의합니다.

## 1. JPA_ENTITY_GENERATOR
- **Description**: 프로젝트 컨벤션에 맞는 JPA Entity 클래스를 생성합니다.
- **Capabilities**:
    - `@Table(name = "{entity_name}_tb")` 명명 규칙 준수.
    - `Integer id` 및 `IDENTITY` 전략 설정.
    - `FetchType.LAZY`를 이용한 모든 연관관계 설정.
    - Lombok 어노테이션 (`@NoArgsConstructor`, `@Data`, `@Builder`) 적용.
    - `@CreationTimestamp`를 이용한 생성 시간 관리.

## 2. DTO_STRUCTURE_ENFORCER
- **Description**: 기능별로 구조화된 내부 클래스 형태의 DTO를 생성합니다.
- **Capabilities**:
    - `Request` 클래스 내부에 `Save`, `Update` 등의 static inner class 생성.
    - `Response` 클래스 내부에 `Detail`, `List` 등의 static inner class 생성.
    - 모든 DTO에 `@Data` 적용 및 기능적 명명 규칙 준수.

## 3. SERVICE_LAYER_IMPLEMENTER
- **Description**: 비즈니스 로직을 구현하고 트랜잭션을 관리합니다.
- **Capabilities**:
    - 클래스 상단 `@Transactional(readOnly = true)` 설정.
    - 쓰기 작업 시 개별 `@Transactional` 적용.
    - **Entity to DTO** 변환 로직을 서비스 레이어에서만 처리.
    - 컨트롤러에 Entity가 노출되지 않도록 강제.

## 4. ARCHITECTURE_VALIDATOR
- **Description**: 작성된 코드가 프로젝트 아키텍처 규칙을 준수하는지 검증합니다.
- **Capabilities**:
    - 패키지 구조가 `package-by-feature`인지 확인.
    - 레이어 간 데이터 흐름(Controller -> Service -> Repository) 준수 여부 확인.
    - OSIV false 환경에서의 지연 로딩 처리 적절성 확인.
