# 프로젝트 코드 컨벤션 (Code Rules)

이 문서는 `demo` 프로젝트의 일관된 코드 작성을 위한 규칙을 정의합니다.

## 1. 아키텍처 및 패키지 구조
- **기능 기반 패키징 (Package by Feature):** `board`, `user` 등 기능 단위로 패키지를 구성합니다.
- **계층 구조:** 각 기능 패키지 내부에는 `Controller`, `Service`, `Repository`, `Entity`, `Request DTO`, `Response DTO`가 포함됩니다.

## 2. 엔티티 (Entity) 규칙
- **테이블 명명:** `@Table(name = "xxx_tb")` 형식을 사용합니다.
- **기본키 (PK):** `Integer id` 타입을 사용하며, `@GeneratedValue(strategy = GenerationType.IDENTITY)`를 적용합니다.
- **Lombok 사용:** `@NoArgsConstructor`, `@Data`, `@Builder`를 기본적으로 사용합니다.
- **연관관계:** 모든 연관관계는 `FetchType.LAZY`로 설정합니다. (OSIV는 `false`로 유지)
- **생성자 규칙:** `@Builder`를 사용할 때 컬렉션 필드는 생성자에 포함하지 않습니다.
- **시간 설정:** `@CreationTimestamp`와 `LocalDateTime`을 사용하여 생성 시간을 관리합니다.

## 3. 컨트롤러 (Controller) 규칙
- **어노테이션:** `@Controller`를 사용합니다.
- **의존성 주입:** `@RequiredArgsConstructor`와 `private final` 필드를 사용하여 주입합니다.
- **반환 타입:** 뷰 이름을 `String`으로 반환하여 머스테치(Mustache) 템플릿과 연결합니다.

## 4. 서비스 (Service) 규칙
- **어노테이션:** `@Service`를 사용합니다.
- **트랜잭션:** 클래스 상단에 `@Transactional(readOnly = true)`를 선언하여 읽기 전용을 기본으로 하고, 변경 작업에만 별도로 `@Transactional`을 적용합니다.
- **DTO 변환:** DTO는 Service 레이어에서 생성하여 반환합니다. **Entity를 Controller로 전달하지 않습니다.**

## 5. 레포지토리 (Repository) 규칙
- **상속:** `JpaRepository<Entity, Integer>`를 상속받는 인터페이스로 구현합니다.

## 6. DTO (Request/Response) 규칙
- **내부 클래스 활용:** `BoardRequest`, `BoardResponse`와 같은 대표 클래스 내부에 `static class`를 사용하여 각 기능별 DTO(Save, Detail 등)를 정의합니다.
- **명명 규칙:** 
    - 요청 DTO: 기능명을 명시합니다. (예: `Save`, `Update`)
    - 응답 DTO: 데이터의 성격을 명시합니다. (예: `Detail`, `List`)
- **Lombok:** DTO에는 `@Data`를 사용합니다.

## 7. 기타 규칙
- **공통 응답:** `_core` 패키지의 유틸리티를 활용하여 일관된 응답 형식을 유지합니다.
