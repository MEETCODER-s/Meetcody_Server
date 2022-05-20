package com.meetcody.meetcodyserver.mobile.dto.oauth;

import com.meetcody.meetcodyserver.domain.user.Oauth;
import com.meetcody.meetcodyserver.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OauthSaveRequestDto {
    private String accessToken;
    private int expiresIn;
    private LocalDateTime created_date;
    private User user;

    public Oauth toEntity() {
        return Oauth.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .created_date(created_date)
                .user(user)
                .build();
    }

}
