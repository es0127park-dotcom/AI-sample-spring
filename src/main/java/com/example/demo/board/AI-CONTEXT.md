<!-- Parent: ../AI-CONTEXT.md -->

# board

## 목적

게시글에 관한 데이터 처리 및 비즈니스 로직을 담당하는 도메인입니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| Board.java | 게시글 엔티티 (`board_tb`) |
| BoardController.java | SSR 방식 게시글 컨트롤러 (Mustache) |
| BoardService.java | 게시글 비즈니스 로직 처리 및 DTO 변환 |
| BoardRepository.java | JPA 데이터 접근 인터페이스 |
| BoardRequest.java | 클라이언트 요청 데이터를 담는 내부 클래스 모음 |
| BoardResponse.java | 서비스에서 반환하는 응답 DTO 모음 |

## AI 작업 지침

- 모든 연관관계(예: User)는 `LAZY` 로딩으로 설정됨.
- 조회 시 `default_batch_fetch_size` 설정을 활용하여 N+1 문제를 방지할 것.
- 서비스에서 엔티티를 직접 컨트롤러로 반환하지 말고, 반드시 DTO로 변환하여 반환할 것.

## 테스트

- `BoardRepositoryTest`: DB 연동 테스트 (DataJpaTest)
- `BoardServiceTest`: 비즈니스 로직 테스트 (Mock기반)

## 의존성

- 내부: `user` 도메인의 `User` 엔티티를 참조 (연관관계).
- 외부: Spring Data JPA, Lombok.

<!-- MANUAL -->
