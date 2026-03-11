# AI Guide

> **Workflow 중심 개발**: 개발자는 명확한 순서를 설계하고, 코드는 AI가 작성한다.

---

## 1. 자동 동기화 (최우선 실행)

이 문서를 읽을 때 반드시 `.ai/skills/*/SKILL.md` 및 `.ai/agents/*/AGENT.md` 파일을 스캔하여 name, description을 추출하고, 아래 테이블을 실제 파일 내용과 일치하도록 자동 갱신해야 한다.

| 분류      | 이름              | 트리거/용도                                                                          | 주요 능력                                                                                  |
| :-------- | :---------------- | :----------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------- |
| **Skill** | `deep-interview`  | `/deep-interview`, `요구사항 정리`, `기능 정의`, `문제 분석`                         | 소크라테스식 인터뷰를 통해 모호한 요구사항을 구조화된 명세로 변환 (코딩 전 문제 정의)      |
| **Skill** | `deepinit`        | `/deepinit`, `코드베이스 문서화해줘`, `AI 가이드 만들어줘`, `AI-CONTEXT.md 생성해줘` | 코드베이스를 인덱싱하고 계층형 AI-CONTEXT.md 파일을 생성하여 프로젝트 구조와 지침을 문서화 |
| **Agent** | `git-auto-commit` | `자동으로 커밋해줘`, `변경 사항 정리해줘`, `git 커밋해줘`                            | 변경 사항을 분석하여 논리적 그룹화 및 Conventional Commit 메시지 자동 생성 및 커밋 실행    |

---

## 2. 참조 규칙 (Memory Load)

아래 파일을 읽고 규칙을 반드시 준수한다.

- **컨벤션**: `.ai/rules/common-rule.md`
- **스킬상세**: `.ai/skills/{스킬명}/SKILL.md`

## 3. 활동 로깅 (Activity Logging)

`.ai/hooks/log-activity.sh`를 통해 `.person/logs/activity.log`에 기록된다.

### 수동 로깅 (커스텀 스킬)

`.ai/skills/`의 커스텀 스킬은 Claude Code `Skill` 도구에 등록되지 않으므로 hook이 자동 트리거되지 않는다.
커스텀 스킬의 SKILL.md를 읽어 실행할 때, **반드시** 아래 명령을 함께 실행하여 로그를 남겨라:

```bash
echo '{"tool_name":"Skill","tool_input":{"skill":"<스킬명>"}}' | bash .ai/hooks/log-activity.sh
```

## 4. AI-CONTEXT

코드 작업 전 해당 디렉토리에 `AI-CONTEXT.md`가 있으면 먼저 읽어라.
