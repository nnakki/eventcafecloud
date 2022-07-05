package com.eventcafecloud.user.controller;

import com.eventcafecloud.oauth.token.AuthTokenProvider;
import com.eventcafecloud.user.domain.User;
import com.eventcafecloud.user.dto.HostUserCreateRequestDto;
import com.eventcafecloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AuthTokenProvider tokenProvider;
    private final UserService userService;

    @GetMapping("/")
    public String main(@CookieValue(required = false, name = "access_token") String token, Model model) {
        if (token != null) {
            String userEmail = tokenProvider.getUserEmailByToken(token);
            User user = userService.getUserByEmail(userEmail);
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register/host")
    public String registerHostForm(@CookieValue(required = false, name = "access_token") String token, Model model) {
        String userEmail = tokenProvider.getUserEmailByToken(token);
        model.addAttribute("hostUserCreateRequestDto", new HostUserCreateRequestDto());
        model.addAttribute("Email", userEmail);
        System.out.println(userEmail);
        return "register/registerHostForm";
    }

    @PostMapping("/register")
    public String saveHostUser(HostUserCreateRequestDto requestDto) {

        userService.saveHostUser(requestDto);

        return "redirect:/";
    }
}
