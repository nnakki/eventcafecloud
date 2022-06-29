package com.eventcafecloud.event.dto;

import com.eventcafecloud.event.domain.Event;
import com.eventcafecloud.event.domain.EventImage;
import com.eventcafecloud.event.domain.type.EventCategory;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class EventCreateResponse {
    private String eventName;
    private EventCategory eventCategory;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private String eventInfo;
    private List<EventImage> eventImage;

    @Builder
    public EventCreateResponse(Event event) {
        this.eventName = event.getEventName();
        this.eventCategory = event.getEventCategory();
        this.eventStartDate = event.getEventStartDate();
        this.eventEndDate = event.getEventEndDate();
        this.eventInfo = event.getEventInfo();
        this.eventImage = event.getEventImage();
    }

    public static EventCreateResponse from(Event event) {
        String eventName = event.getEventName();
        EventCategory eventCategory = event.getEventCategory();
        LocalDate eventStartDate = event.getEventStartDate();
        LocalDate eventEndDate = event.getEventEndDate();
        String eventInfo = event.getEventInfo();
        List<EventImage> eventImage = event.getEventImage();

        EventCreateResponse eventCreateResponse = new EventCreateResponse(eventName, eventCategory, eventStartDate, eventEndDate, eventInfo, eventImage);

        return eventCreateResponse;
    }
}
