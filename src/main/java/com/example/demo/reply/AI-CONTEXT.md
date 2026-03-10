<!-- Parent: ../AI-CONTEXT.md -->

# reply

## 목적

게시글에 달리는 댓글 기능을 담당하는 도메인입니다.

## 주요 파일

| 파일명 | 설명 |
| :--- | :--- |
| Reply.java | 댓글 엔티티 (`reply_tb`) |
| ReplyController.java | 댓글 작성/삭제 관련 컨트롤러 |
| ReplyService.java | 댓글 추가 및 삭제 로직 |
| ReplyRepository.java | 댓글 데이터 접근 인터페이스 |

## AI 작업 지침

- 게시글(`Board`)과 회원(`User`)에 대한 연관관계를 `LAZY`로 유지할 것.
- 댓글 작성 시 부모 게시글 존재 여부를 반드시 검증할 것.
