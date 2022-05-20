package com.meetcody.meetcodyserver.oauth;

import com.meetcody.meetcodyserver.mobile.dto.oauth.OauthSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value ="/auth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    /**
     * 사용자로부터 SNS 로그인 요청을 Social Login Type 을 받아 처리
     *
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info("socialLoginType = " + socialLoginType);
        oauthService.request(socialLoginType);
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * 1. SNS Login 요청하여 Json 형태의 String 문자열 (access_token, refresh_token 등) 받기
     * 2. Json String -> Map으로 변환
     * 3. Map으로 변환된 토큰 정보를 DB에 저장하는 save 메소드 호출
     *
     * @param socialLoginType (GOOGLE)
     * @param code            API Server 로부터 넘어오는 code
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public void callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code) {
        String jsonString = oauthService.requestAccessToken(socialLoginType, code);
        Map tokenDetail = oauthService.convertJsonStringToMap(jsonString);
        OauthSaveRequestDto requestDto = oauthService.convertMapToDto(tokenDetail);
        oauthService.save(requestDto);
    }
}