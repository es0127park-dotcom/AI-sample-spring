package com.example.demo.user;

import com.example.demo._core.utils.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/api/users/username-check")
    public Resp<?> usernameCheck(@RequestParam("username") String username) {
        boolean isDuplicate = userService.usernameCheck(username);
        return Resp.ok(isDuplicate);
    }
}
