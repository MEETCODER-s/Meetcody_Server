package com.meetcody.meetcodyserver.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetcody.meetcodyserver.domain.user.OauthRepository;
import com.meetcody.meetcodyserver.mobile.dto.oauth.OauthSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final OauthRepository oauthRepository;
    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        return socialOauth.requestAccessToken(code);
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    public Map convertJsonStringToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> tokenDetail = mapper.readValue(jsonString, Map.class);
            return tokenDetail;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OauthSaveRequestDto convertMapToDto(Map tokenDetail) {
        String accessToken = (String) tokenDetail.get("access_token");
        int expiresIn = (int) tokenDetail.get("expires_in");

        OauthSaveRequestDto requestDto = OauthSaveRequestDto.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .created_date(LocalDateTime.now())
                .build();

        return requestDto;
    }

    @Transactional
    public Long save(OauthSaveRequestDto requestDto) {
        return oauthRepository.save(requestDto.toEntity()).getId();
    }

}
