package com.meetcody.meetcodyserver.domain.invitation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    INVITER("ROLE_INVITER", "주최자"),
    INVITEE("ROLE_INVITEE", "참석자");

    private final String key;
    private final String title;
}
