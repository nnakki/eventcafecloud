package com.eventcafecloud.cafe.controller;

import com.eventcafecloud.cafe.domain.Cafe;
import com.eventcafecloud.cafe.dto.CafeCreateRequestDto;
import com.eventcafecloud.cafe.service.CafeService;
import com.eventcafecloud.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;
    private final AuthTokenProvider tokenProvider;

    @GetMapping("/cafes/registration")
    public String cafeCreateForm(Model model){

        model.addAttribute("cafeCreateRequestDto", new CafeCreateRequestDto());

        return "cafe/createCafeForm";
    }
    // @RequestBody @Valid CafeRequestDto requestDto, BindingResult result
    // @RequestBody 넣은 경우 Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
    // ajax 호춣이 아니라서 ContentType 지정을 json으로 못함. -> 데이터 타입 에러
    @PostMapping("/cafes")
    public String cafeCreate(@Valid CafeCreateRequestDto requestDto, BindingResult result, @CookieValue(required = false, name = "access_token") String token, Model model){
        if(result.hasErrors()){
            return "cafe/createCafeForm";
        }
        String userEmail = "";
        if(token != null) {
            userEmail = tokenProvider.getUserEmailByToken(token);
            model.addAttribute("userEmail", userEmail);
        }

        cafeService.createCafe(requestDto, userEmail);

        return "redirect:/";
    }

    @GetMapping("/cafes/all")
    public String cafeList(Model model){

        model.addAttribute("cafeCreateRequestDto", new CafeCreateRequestDto());

        return "cafe/cafeList";
    }
}
