package com.meetcody.meetcodyserver.mobile.dto.user;

import com.meetcody.meetcodyserver.domain.user.User;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSaveRequestDto {
    private String familyName;
    private String givenName;
    private String username;
    private String email;
    private String phone;
    private String picture;

    public User toEntity() {
        return User.builder()
                .familyName(familyName)
                .givenName(givenName)
                .username(username)
                .email(email)
                .phone(phone)
                .picture(picture)
                .build();
    }
}
