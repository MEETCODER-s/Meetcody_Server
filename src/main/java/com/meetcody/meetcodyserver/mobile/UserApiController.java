package com.meetcody.meetcodyserver.mobile;

import com.meetcody.meetcodyserver.domain.friend.FriendRepository;
import com.meetcody.meetcodyserver.domain.user.User;
import com.meetcody.meetcodyserver.domain.user.UserRepository;
import com.meetcody.meetcodyserver.mobile.dto.user.UserSaveRequestDto;
import com.meetcody.meetcodyserver.mobile.dto.user.UserUpdateRequestDto;
import com.meetcody.meetcodyserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RequestMapping("/v1/user")
@RestController
@AllArgsConstructor
public class UserApiController {

    public final UserService userService;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @PostMapping(value = "/signup")
    public HashMap<String, String> save(@RequestBody HashMap<String,String> userinfo) {

        String number=userinfo.get("phone");
        System.out.println(number);
        if(number.contains(") "))
            number =number.substring(number.lastIndexOf(") ")+2);

        if(!number.contains("-")){
            String tmp1 = number.substring(0,3);
            String tmp2 = number.substring(3,7);
            String tmp3 = number.substring(7);
            number = tmp1+"-"+ tmp2+"-"+tmp3;
            System.out.println(number);
        }

        List<User> users = friendRepository.findUserByPhone(number);

        HashMap<String,String> tmp = new HashMap<>();
        if(users.size()==1){ //이미 회원이라면
            tmp.put("userid", String.valueOf(users.get(0).getId()));

            tmp.put("email", users.get(0).getEmail());
            tmp.put("nickname", users.get(0).getUsername());
            tmp.put("phonenum", users.get(0).getPhone());
            return tmp;
        }
        else{
            User user =User.builder().email(userinfo.get("email")).familyName(userinfo.get("familyName")).givenName(userinfo.get("givenName")).phone(userinfo.get("phone")).picture(userinfo.get("picture")).username(userinfo.get("username")).build();
            userRepository.save(user);
            tmp.put("userid", String.valueOf(user.getId()));
            tmp.put("email", user.getEmail());
            tmp.put("nickname", user.getUsername());
            tmp.put("phonenum", user.getPhone());

            return tmp;
        }
    }

    @PostMapping(value = "/login")
    public Long login(@RequestBody UserSaveRequestDto requestDto) {
        return userService.save(requestDto);
    }

    @PutMapping(value = "/user")
    public Long update(@PathVariable Long id, @RequestBody UserUpdateRequestDto requestDto) {
        return userService.update(id, requestDto);
    }

    @PostMapping(value = "/checkemail")
    public HashMap<String, String> checkEmainExist(@RequestBody HashMap<String, List<String>> userinfo) {
        List<User> user =userRepository.findUserByEmail(userinfo.get("email").get(0));
        HashMap<String, String> post= new HashMap<>();
        if (user.size()!=0){
            post.put("email", user.get(0).getEmail());
            post.put("id", String.valueOf(user.get(0).getId()));
            post.put("nickname", user.get(0).getUsername());
            post.put("phonenum", user.get(0).getPhone());
        }
        return post;
    }
}

