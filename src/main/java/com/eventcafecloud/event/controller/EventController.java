package com.eventcafecloud.event.controller;

import com.eventcafecloud.event.dto.EventCreateRequestDto;
import com.eventcafecloud.event.dto.EventListResponseDto;
import com.eventcafecloud.event.dto.EventReadResponseDto;
import com.eventcafecloud.event.dto.EventUpdateRequestDto;
import com.eventcafecloud.event.service.EventService;
import com.eventcafecloud.oauth.token.AuthTokenProvider;
import com.eventcafecloud.user.domain.User;
import com.eventcafecloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AUTH;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final AuthTokenProvider tokenProvider;

    // 이벤트 예약 폼
    @Secured("ROLE_NORMAL")
    @GetMapping("/events/registration")
    public String createEventForm(Model model) {
        model.addAttribute("eventCreateRequestDto", new EventCreateRequestDto());
        return "event/createEventForm";
    }

    // 이벤트 예약
    @PostMapping("/events/registration")
    public String createEvent(@CookieValue(required = false, name = "access_token") String token,
                              @Validated @ModelAttribute EventCreateRequestDto requestDto, BindingResult result) {
        if (token != null) {
            String email = tokenProvider.getUserEmailByToken(token);
            eventService.saveEvent(requestDto, email);

            if (result.hasErrors()) {
                return "event/createEventForm";
            } else {
                return "redirect:/events";
            }
        }
        return "redirect:/events";
    }

    // 이벤트 수정 폼
    @GetMapping("/events/{eventNumber}/edit")
    public String updateEventForm(@PathVariable Long eventNumber, Model model){
        model.addAttribute("eventUpdateRequestDto", new EventUpdateRequestDto());
        model.addAttribute("event", eventService.getEventById(eventNumber));
        return "event/updateEventForm";
    }

    // 이벤트 수정
    @PostMapping("/events/{eventNumber}/edit")
    public String updateEvent(@PathVariable Long eventNumber, @Validated @ModelAttribute EventUpdateRequestDto requestDto, BindingResult result) {
        eventService.modifyEvent(eventNumber, requestDto);
        return "redirect:/events/{eventNumber}";
    }

    // 이벤트 삭제
    @DeleteMapping("/events/{eventNumber}/cancle")
    public String cancleEvent(@PathVariable Long eventNumber) {
        eventService.removeEvent(eventNumber);
        return "redirect:/events";
    }

    // 이벤트 리스트 보기
    @GetMapping("/events")
    public String eventList(Model model) {
        List<EventListResponseDto> eventListResponseDtos = eventService.findEventList();
        model.addAttribute("eventListResponseDtos", eventListResponseDtos);
        return "event/eventList";
    }

    // 이벤트 상세
    @GetMapping("/events/{eventNumber}")
    public String eventDetail(@CookieValue(required = false, name = "access_token") String token,
                              @PathVariable Long eventNumber, Model model) {
        if (token != null) {
            String userEmail = tokenProvider.getUserEmailByToken(token);
            User user = userService.getUserByEmail(userEmail);
            model.addAttribute("user", user);
        }

        EventReadResponseDto eventReadResponseDto = eventService.findEvent(eventNumber);
        model.addAttribute("eventReadResponseDto", eventReadResponseDto);
        model.addAttribute("event", eventService.getEventById(eventNumber));
        return "event/eventDetail";
    }
}
