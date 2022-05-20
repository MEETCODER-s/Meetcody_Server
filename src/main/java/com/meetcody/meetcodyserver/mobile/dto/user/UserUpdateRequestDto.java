package com.meetcody.meetcodyserver.mobile.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String familyName;
    private String givenName;
    private String username;
    private String email;
    private String phone;
    private String picture;
}
