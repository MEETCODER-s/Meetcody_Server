package com.meetcody.meetcodyserver.mobile.dto.calendar;

import com.meetcody.meetcodyserver.domain.calendar.Calendar;
import lombok.Getter;

@Getter
public class CalendarResponseDto {

    private Long id;
    private String GoogleCalId;
    private String etag;
    private String timezone;
    private Boolean isHangoutsmeet;

    public CalendarResponseDto(Calendar entity) {
        this.id = entity.getId();
        this.GoogleCalId = entity.getGoogleCalId();
        this.etag = entity.getEtag();
        this.timezone = entity.getTimezone();
        this.isHangoutsmeet = entity.getIsHangoutsmeet();
    }
}
