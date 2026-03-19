package com.example.demo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid UserRequest.Join reqDTO, BindingResult bindingResult) {
        userService.join(reqDTO);
        return "redirect:/login-form"; // 로그인 페이지가 아직 없으므로 추후 구현 예정
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
        var user = userService.login(reqDTO);
        session.setAttribute("sessionUser", user);
        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    // 회원가입 페이지 반환
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    // 로그인 페이지 반환
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    // 회원 정보 수정 페이지 반환
    @GetMapping("/user/update-form")
    public String updateForm(Model model) {
        var sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        var user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    // 회원 정보 수정 처리
    @PostMapping("/user/update")
    public String update(@Valid UserRequest.Update reqDTO, BindingResult bindingResult) {
        var sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        var updatedUser = userService.update(sessionUser.getId(), reqDTO);
        session.setAttribute("sessionUser", updatedUser);
        return "redirect:/user/update-form";
    }

    // 회원 탈퇴 처리
    @PostMapping("/user/delete")
    public String delete() {
        var sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        userService.delete(sessionUser.getId());
        session.invalidate();
        return "redirect:/";
    }
}
