package com.meetcody.meetcodyserver.domain.invitation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Form {

    ONLINE("FORM_ONLINE", "온라인"),
    OFFLINE("FORM_OFFLINE", "오프라인");

    private final String key;
    private final String title;

}
