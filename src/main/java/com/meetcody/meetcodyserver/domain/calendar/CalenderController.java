package com.meetcody.meetcodyserver.domain.calendar;

import com.meetcody.meetcodyserver.domain.user.Oauth;
import com.meetcody.meetcodyserver.domain.user.OauthRepository;
import com.meetcody.meetcodyserver.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalenderController {

    public final CalendarService calendarService;

    private final OauthRepository oauthRepository;

    private final String[] accesstocken = {""};

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/calendergettest",consumes="application/json; charset=utf8")
    public @ResponseBody String checkUserOuath (@RequestBody HashMap<String, String> userinfo){

        //일단 입력된 이메일로된 액세스 토큰 중 유요한게 데이터베이스의 oauth테이블에 있는지 확인한다.
        List<Oauth> findOauthByEmailList = oauthRepository.findOauthByEmail(userinfo.get("email"));



        if(findOauthByEmailList.size()==0){
            //액세스 토큰이 없으니 생성해주기

        }
        else{
            findOauthByEmailList.forEach(Oauths->{
//                액세스 토큰이 있으니 기존의 토큰이 유효한지 확인
                if(Oauths.getCreated_date().plusSeconds(Oauths.getExpiresIn()).isBefore(LocalDateTime.now())){
                    oauthRepository.deleteByExpiresIn(Oauths.getId());//기간 만료된거면 삭제
                    System.out.println("기간이 만료된 토큰입니다.");
                }
                else {
                    accesstocken[0] =Oauths.getAccessToken();
                }
            });
        }

        calendarService.getAvailableDateList(accesstocken[0]);
        return "found";
    }

}
