package com.meetcody.meetcodyserver.domain.invitation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    ACCEPT("STATUS_ACCEPT", "수락"),
    DECLINE("STATUS_DECLINE", "거절"),
    NEEDACTION("STATUS_NEEDACTION", "응답필요");
    
    private final String key;
    private final String title;
}
