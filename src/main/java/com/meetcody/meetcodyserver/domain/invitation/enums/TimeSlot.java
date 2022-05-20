package com.meetcody.meetcodyserver.domain.invitation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeSlot {

    DAWN("TIME_SLOT_DAWN", "새벽(0-7시)"),
    MORNING("TIME_SLOT_MORNING", "아침(7-11시)"),
    AFTERNOON("TIME_SLOT_AFTERNOON", "점심(11-17시)"),
    NIGHT("TIME_SLOT_NIGHT", "저녁(17-24시)");

    private final String key;
    private final String title;
}
