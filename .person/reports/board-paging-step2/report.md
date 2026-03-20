# 🚩 작업 보고서: 게시글 페이징 단계별 학습 (Step 2)

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료 (Step 2: SQL 기초 페이징 - LIMIT/OFFSET)

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **Repository 쿼리 설계**: Hibernate 6부터 지원하는 JPQL 내 `LIMIT`와 `OFFSET` 구문을 활용하여 최신순으로 데이터를 끊어오는 쿼리를 작성했습니다.
2. **Service 페이징 연산**: 페이징의 핵심인 `offset = page * size` 공식을 Java 로직으로 직접 구현하여, 특정 페이지에서 스킵해야 할 데이터의 양을 계산했습니다.
3. **Controller 파라미터 연동**: 웹 브라우저로부터 `page` 파라미터를 입력받아 서비스에 전달하고, 데이터가 3개씩 필터링되어 출력되도록 연결했습니다.

## 2. 🧩 핵심 코드 (Core Logic)

### 🖥️ Backend: BoardRepository.java
```java
// Hibernate 6 확장 JPQL: 엔티티 대상 LIMIT/OFFSET 사용
@Query("SELECT b FROM Board b ORDER BY b.id DESC LIMIT :limit OFFSET :offset")
List<Board> findAll(@Param("limit") int limit, @Param("offset") int offset);
```

### 🖥️ Backend: BoardService.java
```java
public List<BoardResponse.ListDTO> 게시글목록보기(int page) {
    int size = 3; // 한 페이지당 3개씩
    int limit = size;
    int offset = page * size; // 핵심 공식: 현재 페이지 * 페이지 크기
    
    List<Board> boards = boardRepository.findAll(limit, offset);
    return boards.stream().map(BoardResponse.ListDTO::new).toList();
}
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **'도서 목록 카드를 3장씩 묶어 정리하는 것'**과 같습니다.
1. **규칙 정하기**: '한 묶음에 무조건 3장씩!'이라는 규칙을 정했습니다(`size = 3`).
2. **건너뛰기**: 만약 2번 묶음(page 1)을 보고 싶다면, 앞의 3장(0번 묶음)을 건너뛰고(`offset = 3`) 그 다음 3장(`limit = 3`)을 가져오는 것과 같습니다.
3. **번호표 받기**: 사용자가 '나 2번 묶음 보여줘'라고 하면(/?page=1), 사서(Controller)가 계산해서 딱 맞는 3장의 카드를 꺼내주는 시스템을 구축한 것입니다."

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **Hibernate 6 LIMIT/OFFSET**: 기존 JPA 표준에서는 JPQL에 `LIMIT`를 쓸 수 없어 Native Query나 `setFirstResult`, `setMaxResults`를 써야 했습니다. Hibernate 6는 이를 JPQL 레벨에서 직접 지원하여 가독성을 크게 높였습니다.
- **Paging Algorithm**: 페이징의 기본은 **'어디서부터(Offset)'**, **'몇 개(Limit)'** 가져올지를 결정하는 것입니다. 이 수동 계산 과정을 통해 프레임워크가 내부적으로 처리하는 수학적 원리를 이해할 수 있습니다.
