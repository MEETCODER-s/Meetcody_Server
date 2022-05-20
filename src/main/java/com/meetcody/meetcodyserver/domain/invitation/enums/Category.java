package com.meetcody.meetcodyserver.domain.invitation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    BUSINESS("CATEGORY_BUSINESS", "비즈니스"),
    PRIVATE("CATEGORY_PRIVATE", "사적모임");

    private final String key;
    private final String title;
}
