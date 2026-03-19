# 🚩 작업 보고서: T-2.2 로그인/로그아웃 기능 및 화면 구현

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

```text
+---------------------------------------------------------+
| [Header Navbar] (Blog | 회원가입/로그인 OR 글쓰기/로그아웃) |
+---------------------------------------------------------+
|                                                         |
|      +-------------------------------------------+      |
|      |                  로그인                   |      |
|      |-------------------------------------------|      |
|      |                                           |      |
|      |  아이디                                   |      |
|      |  [ input: username                      ] |      |
|      |                                           |      |
|      |  비밀번호                                 |      |
|      |  [ input: password                      ] |      |
|      |                                           |      |
|      |         [    로그인하기 버튼    ]         |      |
|      |                                           |      |
|      +-------------------------------------------+      |
|                                                         |
+---------------------------------------------------------+
| [Footer] (Copyright © ...)                              |
+---------------------------------------------------------+
```

1. **DTO 유효성 추가**: `UserRequest.Login`에 아이디/비밀번호 필수 입력 및 길이 제한(`@NotBlank`, `@Size`)을 설정했습니다.
2. **서비스 로직 구현**: `UserService.login`에서 `UserRepository`로 유저를 찾고, `PasswordEncoder.matches`로 암호화된 비밀번호를 검증했습니다.
3. **컨트롤러 연동**: `UserController`에 로그인(성공 시 세션 저장) 및 로그아웃(세션 무효화) 핸들러를 추가했습니다.
4. **UI 구현**: 부트스트랩을 사용하여 깔끔한 로그인 폼을 만들고, 헤더 내비게이션 바가 세션 상태(`sessionUser`)에 따라 동적으로 변하게 수정했습니다.

## 2. 🧩 변경된 모든 코드 포함

### UserRequest.java (Login DTO)
```java
@Data
public static class Login {
    @NotBlank(message = "유저네임은 필수입니다")
    @Size(min = 4, max = 20, message = "유저네임은 4자에서 20자 사이여야 합니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 4, max = 20, message = "비밀번호는 4자에서 20자 사이여야 합니다")
    private String password;
}
```

### UserService.java (로그인 로직)
```java
public User login(UserRequest.Login reqDTO) {
    // 1. 아이디로 유저 찾기
    var userOp = userRepository.findByUsername(reqDTO.getUsername());
    if (userOp.isEmpty()) {
        throw new Exception400("아이디 혹은 비밀번호가 틀렸습니다.");
    }
    var user = userOp.get();

    // 2. 비밀번호 매칭 확인 (암호화된 비번 vs 평문 비번)
    if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
        throw new Exception400("아이디 혹은 비밀번호가 틀렸습니다.");
    }
    return user;
}
```

### UserController.java (핸들러)
```java
@PostMapping("/login")
public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
    // 비즈니스 로직 호출
    var user = userService.login(reqDTO);
    // 세션에 로그인 정보 저장 (마치 놀이공원 자유이용권을 채워주는 것과 같음)
    session.setAttribute("sessionUser", user);
    return "redirect:/";
}

@GetMapping("/logout")
public String logout() {
    // 세션 무효화 (자유이용권 회수)
    session.invalidate();
    return "redirect:/";
}
```

### header.mustache (동적 메뉴)
```html
<ul class="navbar-nav">
    {{#sessionUser}} <!-- 세션 유저가 있으면 보여줄 메뉴 -->
        <li class="nav-item"><a class="nav-link" href="/board/save-form">글쓰기</a></li>
        <li class="nav-item"><a class="nav-link" href="/logout">로그아웃</a></li>
    {{/sessionUser}}
    {{^sessionUser}} <!-- 세션 유저가 없으면 보여줄 메뉴 -->
        <li class="nav-item"><a class="nav-link" href="/join-form">회원가입</a></li>
        <li class="nav-item"><a class="nav-link" href="/login-form">로그인</a></li>
    {{/sessionUser}}
</ul>
```

## 3. 🍦 상세비유 쉬운 예시를 들어서 (Easy Analogy)
"이번 작업은 **'놀이공원 입장 시스템'**과 같습니다.
- **로그인**은 입구에서 매표원이 신분증(ID)과 얼굴(PW)을 대조하여 확인한 뒤, **'자유이용권(Session)'** 팔찌를 채워주는 것과 같아요.
- **헤더의 동적 메뉴**는 놀이공원 안에서 '자유이용권 팔찌'가 있는 사람에게만 '무서운 놀이기구(글쓰기/수정)' 입장을 허용하는 검표원과 같습니다.
- **로그아웃**은 집으로 돌아갈 때 팔찌를 끊어서 버리는 것과 비슷합니다. 팔찌가 없으면 다시 입장할 수 없게 되죠!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **HttpSession**: 서버가 클라이언트를 식별하기 위해 사용하는 브라우저별 메모리 공간입니다. 로그인이 성공하면 유저 객체를 여기에 담아두고, 이후 요청마다 "너 누구니?"라고 물어볼 필요 없이 세션을 확인하여 인증 상태를 유지합니다.
- **BCrypt PasswordEncoder**: 비밀번호를 날것 그대로 저장하지 않고, 복호화가 불가능한 해시값으로 변환하여 저장합니다. `matches` 함수는 입력받은 평문 비밀번호와 DB의 해시값을 안전하게 비교해줍니다.
- **Mustache Conditional Rendering**: `{{#sessionUser}}`와 `{{^sessionUser}}`를 사용하여 서버에서 넘어온 데이터의 유무에 따라 HTML의 특정 부분을 보여주거나 숨길 수 있습니다. 이를 통해 로그인 전후의 사용자 경험을 다르게 설계할 수 있습니다.
