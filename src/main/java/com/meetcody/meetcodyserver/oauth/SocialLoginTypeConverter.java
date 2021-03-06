package com.meetcody.meetcodyserver.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
    /*
    SocialLoginTypeConverter는 Controller에서
    socialLoginType 파라미터(@PathVariable을 통해)를 받는데
    enum 타입의 대문자 값을 소문자로 mapping 가능하도록 하기위하여 생성해주었습니다.
     */
    @Override
    public SocialLoginType convert(String s) {
        return SocialLoginType.valueOf(s.toUpperCase());
    }
    }
