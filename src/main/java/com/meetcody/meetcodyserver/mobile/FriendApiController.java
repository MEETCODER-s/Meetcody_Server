package com.meetcody.meetcodyserver.mobile;

import com.meetcody.meetcodyserver.domain.friend.FriendRepository;
import com.meetcody.meetcodyserver.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/v1/friend")
public class FriendApiController {

//    @CrossOrigin(origins = "http://localhost:3000")
//    @PostMapping(value = "/user_contacts",consumes="application/json; charset=utf8")

    private final FriendRepository friendRepository;
    private final FriendService friendService ;

    @ResponseBody
    @PostMapping(value = "/contacts",consumes="application/json; charset=utf8")
    public   List<Long> getUserContactsList ( @RequestBody HashMap<String, List<String>> userinfo) {

        System.out.println(userinfo.get("list"));


        return friendService.getContactsListConvertPhonenum(userinfo.get("list"), Long.valueOf(0)).get("isFriendsUser");
    }

    @ResponseBody
    @PostMapping(value = "/testings" )
    public  String testings () {
        System.out.println("dddd");
        return "hi";
    }

}
