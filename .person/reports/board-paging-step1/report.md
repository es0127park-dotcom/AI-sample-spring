# 🚩 작업 보고서: 게시글 페이징 단계별 학습 (Step 1)

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료 (Step 1: 데이터 준비 및 기본 목록 보기)

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **데이터 환경 구축**: `data.sql`에 21개의 게시글 데이터를 추가하여 페이징을 테스트할 수 있는 충분한 환경을 마련했습니다.
2. **DTO 설계**: 엔티티를 직접 노출하지 않기 위해 `BoardResponse.ListDTO`를 설계하여 필요한 필드(id, title)만 정의했습니다.
3. **Repository 확장**: 최신순 조회를 위해 `findAllOrderByCreatedByDesc` 메서드를 추가하고 JPQL을 통해 `id DESC` 정렬을 구현했습니다.
4. **Service 로직 구현**: `게시글목록보기`라는 한글 메서드명을 사용하여 가독성을 높이고, 엔티티 리스트를 DTO 리스트로 변환하는 스트림 로직을 작성했습니다.
5. **Controller & View**: 홈 경로(`/`)에서 `var` 키워드를 사용하여 목록을 조회하고, `list.mustache` 화면에 Mustache 문법으로 데이터를 출력했습니다.

## 2. 🧩 핵심 코드 (Core Logic)

### 🖥️ Backend: BoardService.java
```java
// 한글 메서드명을 사용한 직관적인 서비스 로직
public List<BoardResponse.ListDTO> 게시글목록보기() {
    // 1. 최신순으로 엔티티 목록 조회
    List<Board> boards = boardRepository.findAllOrderByCreatedByDesc();
    // 2. 엔티티를 DTO로 변환하여 반환 (Stream API 활용)
    return boards.stream().map(BoardResponse.ListDTO::new).toList();
}
```

### 🎨 Frontend: list.mustache
```html
<!-- Mustache 문법을 활용한 목록 출력 -->
<tbody>
{{#boardList}}
    <tr>
        <td>{{id}}</td>
        <td>{{title}}</td>
        <td>
            <a href="/board/{{id}}" class="btn btn-warning btn-sm">상세보기</a>
        </td>
    </tr>
{{/boardList}}
</tbody>
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **'도서관의 전체 도서 목록표'**를 만드는 것과 같습니다.
1. **서가 채우기**: 빈 서가에 책(데이터)을 20권 넘게 꽂아두었습니다(`data.sql`).
2. **목록 카드 작성**: 책의 모든 내용이 아닌, 제목과 번호만 적힌 깔끔한 카드(DTO)를 만들었습니다.
3. **게시판 부착**: 도서관 입구 게시판(list.mustache)에 이 카드들을 순서대로 붙여서 사람들이 한눈에 볼 수 있게 한 과정입니다. 아직은 책이 많아서 목록이 아주 길게 늘어져 있는 상태입니다."

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **JPQL (Java Persistence Query Language)**: 엔티티 객체를 대상으로 쿼리를 작성하는 기술입니다. `select b from Board b order by b.id desc`와 같이 테이블이 아닌 엔티티를 대상으로 정렬 조건을 명시했습니다.
- **Stream API (Java 8+)**: 컬렉션 데이터를 함수형 스타일로 처리하는 도구입니다. `map`을 사용하여 `Board` 엔티티를 `ListDTO`로 우아하게 변환했습니다.
- **var (Java 10+)**: 지역 변수 타입 추론 키워드입니다. 컴파일러가 대입되는 값을 보고 타입을 자동으로 결정해주어 코드가 간결해집니다.
