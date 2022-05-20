package com.meetcody.meetcodyserver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    // in spring security, prefix of key value should be "ROLE_"
    // ',' not ';' !!!!!!!!!!
    GUEST("ROLE_GUEST", "비회원"),
    USER("ROLE_USER", "일반회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;

}
