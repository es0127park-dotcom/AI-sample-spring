# 작업 요약: 아이디(Username) 중복 체크 기능 구현

이 문서는 `person/workflow.md`에 정의된 아이디 중복 체크 기능을 구현하기 위해 진행된 전체 작업 내용을 요약합니다.

## 1. 요구사항 분석 및 설계 구체화

- **목표**: 회원가입 페이지에서 사용자가 입력한 아이디가 이미 사용 중인지 실시간으로 검사하는 기능 구현
- **설계**: `AI-GUIDE.md`의 규칙에 따라 `_docs/person/workflow.md` 문서를 다음과 같이 구체화했습니다.
    - REST API 엔드포인트, 요청/응답 형식 명확화
    - Controller, Service, Repository 각 계층의 역할과 메서드 시그니처 정의
    - 프론트엔드(Mustache, JavaScript)의 동작 방식 상세 기술

## 2. 백엔드 개발

### UserApiController 추가
- **파일**: `src/main/java/com/example/demo/user/UserApiController.java`
- **내용**: 아이디 중복 체크 요청을 처리하는 REST 컨트롤러를 생성했습니다.
    - `GET /api/users/username-check` 엔드포인트 구현
    - `UserService`를 호출하여 중복 여부를 `boolean` 값으로 받은 후, `Resp.ok()`로 감싸 JSON 형태로 응답합니다.

### UserService 로직 추가
- **파일**: `src/main/java/com/example/demo/user/UserService.java`
- **내용**: 실제 비즈니스 로직을 수행하는 `usernameCheck` 메서드를 추가했습니다.
    - `UserRepository`의 `findByUsername` 메서드를 호출하고, 유저 존재 여부(`Optional.isPresent()`)를 반환하도록 하여 이전에 있던 버그를 수정했습니다.

### Resp 유틸리티 수정
- **문제**: `resp.java` 파일명과 클래스명이 Java 컨벤션에 맞지 않아 컴파일 오류가 발생했습니다.
- **조치**: `Resp.java`로 이름을 변경하고, 프로젝트 가이드에 맞는 표준 응답(`status`, `msg`, `body`)을 생성할 수 있도록 `ok()`, `fail()` 정적 메서드를 포함한 제네릭 클래스로 수정했습니다.

## 3. 프론트엔드 개발

### 회원가입 페이지 추가
- **파일**: `src/main/resources/templates/join-form.mustache`
- **내용**: 기존에 없던 회원가입 페이지를 새로 만들었습니다. 아이디, 비밀번호, 이메일을 입력받는 폼과 '중복확인' 버튼을 포함합니다.
- **컨트롤러**: `UserController`에 `/join-form` GET 요청을 처리하여 이 페이지를 렌더링하는 `joinForm()` 메서드를 추가했습니다.

### 아이디 중복 체크 스크립트
- **파일**: `join-form.mustache` 내 `<script>` 태그
- **내용**: '중복확인' 버튼 클릭 시 `fetch` API를 사용하여 백엔드에 비동기 요청을 보냅니다.
    - 응답 결과(`true`/`false`)에 따라 "사용 가능한 아이디입니다." 또는 "이미 사용중인 아이디입니다." 라는 메시지를 사용자에게 보여줍니다.

## 4. 테스트 및 빌드 수정

### 통합 테스트 작성
- **파일**: `src/test/java/com/example/demo/user/UserApiControllerTest.java`
- **내용**: `MockMvc`를 사용하여 API 엔드포인트를 테스트하는 코드를 작성했습니다.
    - **테스트 케이스 1**: 이미 존재하는 아이디(`ssar`)로 요청 시 `true`를 반환하는지 검증
    - **테스트 케이스 2**: 존재하지 않는 아이디로 요청 시 `false`를 반환하는지 검증

### 빌드(build.gradle) 오류 수정
- **문제**: 잘못된 Spring Boot 버전과 테스트 의존성으로 인해 테스트 실행이 불가능했습니다.
- **조치**:
    - Spring Boot 버전을 프로젝트 컨텍스트에 명시된 `3.3.4`로 수정했습니다.
    - 불필요하고 잘못된 테스트 의존성을 제거하고, 표준인 `spring-boot-starter-test`로 통합하여 `MockMvc` 관련 문제를 해결했습니다.

## 5. 최종 검증

- `./gradlew test` 명령을 실행하여 모든 테스트가 성공적으로 통과하고 빌드가 성공하는 것을 확인했습니다.

이상의 과정을 통해 '아이디 중복 체크' 기능 구현을 완료했습니다.
