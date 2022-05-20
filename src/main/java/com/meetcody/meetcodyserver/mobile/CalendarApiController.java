package com.meetcody.meetcodyserver.mobile;

import com.meetcody.meetcodyserver.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/v1/calendar")
public class CalendarApiController {

    public final CalendarService calendarService;
    /**
     * Google API Server에 Google Calendar List를 요청
     * @return Json 형태의 String 문자열(etag, nextPageToken, nextSyncToken, items 등)
     */
    @GetMapping(value = "/list")
    public String calendarList() {
        return calendarService.requestCalendarList();
    }
}

// TODO("세션 유저 구현")
//    @CrossOrigin(origins = "http://localhost:3000")
//    @GetMapping(value= "/calendar/{id}")
//    public CalendarResponseDto findById (@PathVariable String )

