package com.eventcafecloud.event.controller;

import com.eventcafecloud.event.domain.type.EventCategory;
import com.eventcafecloud.event.dto.EventCreateRequestDto;
import com.eventcafecloud.event.dto.EventListResponseDto;
import com.eventcafecloud.event.dto.EventReadResponseDto;
import com.eventcafecloud.event.dto.EventUpdateRequestDto;
import com.eventcafecloud.event.repository.EventRepository;
import com.eventcafecloud.event.service.EventService;
import com.eventcafecloud.oauth.token.AuthTokenProvider;
import com.eventcafecloud.user.domain.User;
import com.eventcafecloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final AuthTokenProvider tokenProvider;
    private final EventRepository eventRepository;

    // 이벤트 예약 폼
    @Secured("ROLE_NORMAL")
    @GetMapping("/events/registration")
    public String createEventForm(User loginUser, Model model, @RequestParam Long cafeId) {

        if (loginUser != null) {
            model.addAttribute("userNick", loginUser.getUserNickname());
            model.addAttribute("userId", loginUser.getId());
            model.addAttribute("cafeId", cafeId);
        }
        model.addAttribute("eventCreateRequestDto", new EventCreateRequestDto());
        return "event/createEventForm";
    }

    // 이벤트 예약
    @PostMapping("/events")
    public String createEvent(User loginUser, @Validated @ModelAttribute EventCreateRequestDto requestDto, BindingResult result) {

        eventService.saveEvent(requestDto, loginUser);

        if (result.hasErrors()) {
            return "event/createEventForm";
        } else {
            return "redirect:/events";
        }
    }

    // 이벤트 수정 폼
    @GetMapping("/events/{eventNumber}/edit")
    public String updateEventForm(@PathVariable Long eventNumber, Model model){
        model.addAttribute("eventUpdateRequestDto", new EventUpdateRequestDto());
        model.addAttribute("event", eventService.getEventById(eventNumber));
        return "updateEventModal";
    }

    // 이벤트 수정
    @PostMapping("/events/{eventNumber}/edit")
    public String updateEvent(@PathVariable Long eventNumber, @Validated @ModelAttribute EventUpdateRequestDto requestDto, BindingResult result) {
        eventService.modifyEvent(eventNumber, requestDto);
        return "redirect:/events/{eventNumber}/detail";
    }

    // 이벤트 삭제
    @DeleteMapping("/events/{eventNumber}/cancle")
    public String cancleEvent(@PathVariable Long eventNumber) {
        eventService.removeEvent(eventNumber);
        return "redirect:/events";
    }

    // 이벤트 리스트 보기
    @GetMapping("/events")
    public String eventList(@PageableDefault(size = 10) Pageable pageable,
                            @RequestParam(required = false, defaultValue = "", value="keyword") String keyword,
                            @RequestParam(required = false, value="eventCategory") EventCategory eventCategory,
                            User loginUser, Model model) {

        if (loginUser != null) {
            model.addAttribute("userNick", loginUser.getUserNickname());
            model.addAttribute("userId", loginUser.getId());
        }

        Page<EventListResponseDto> eventListResponseDtos = eventService.toDtoList(keyword, eventCategory, pageable);

        int start = Math.max(1, eventListResponseDtos.getPageable().getPageNumber() - 10);
        int last = Math.min(eventListResponseDtos.getTotalPages(), eventListResponseDtos.getPageable().getPageNumber() + 10);

        model.addAttribute("start", start);
        model.addAttribute("last", last);
        model.addAttribute("eventListResponseDtos", eventListResponseDtos);

        return "event/eventList";
    }

    // 이벤트 상세
    @GetMapping("/events/{eventNumber}/detail")
    public String eventDetail(User loginUser, @PathVariable Long eventNumber, Model model) {
        if (loginUser != null) {
            model.addAttribute("userNick", loginUser.getUserNickname());
            model.addAttribute("userId", loginUser.getId());
        }

        EventReadResponseDto eventReadResponseDto = eventService.findEvent(eventNumber);
        model.addAttribute("eventReadResponseDto", eventReadResponseDto);
        model.addAttribute("event", eventService.getEventById(eventNumber));
        return "event/eventDetail";
    }
}
