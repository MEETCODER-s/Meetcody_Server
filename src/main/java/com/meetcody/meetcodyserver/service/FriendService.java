package com.meetcody.meetcodyserver.service;

import com.meetcody.meetcodyserver.domain.BaseTimeEntity;
import com.meetcody.meetcodyserver.domain.friend.FriendRepository;
import com.meetcody.meetcodyserver.domain.user.User;
import com.meetcody.meetcodyserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashMap;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;



@RequiredArgsConstructor
@Service
public class FriendService  {

   public final UserRepository userRepository;
   private final FriendRepository friendRepository;

//    @Transactional
//    public Long save(ProfileSaveRequestDto requestDto) {
//        return profileRepository.save(requestDto.toEntity()).getId();
//    }

   public HashMap< String, List<Long> > getContactsListConvertPhonenum( List<String> phoneList, Long userid){
      List<Long> isFriendsUser= new ArrayList<>();
      List<Long> friendsIdList = new ArrayList<>();
      List<String> nothipynphonelist = new ArrayList<>();


      System.out.println(phoneList);

      phoneList.forEach(phone->{
         String number=phone;
         if(phone.contains(") "))
            number =phone.substring(phone.lastIndexOf(") ")+2);

         if(!number.contains("-")){
            String tmp1 = number.substring(0,3);
            String tmp2 = number.substring(3,7);
            String tmp3 = number.substring(7);
            number = tmp1+"-"+ tmp2+"-"+tmp3;
            System.out.println(number);
         }

         List<User> users = friendRepository.findUserByPhone(number);
         isFriendsUser.add(Long.valueOf(users.size()));
         if(users.size()==1){
            User user = users.get(0);
            friendsIdList.add(user.getId());
            System.out.println(user.getId()+user.getGivenName());
         }


      });

      final int[] count = {0};
      nothipynphonelist.forEach(phone->{
         if(userid!=Long.valueOf(0)) {
            String api_key = "NCSYYV3L6CBGGMHL";
            String api_secret = "ODELT6G3YVUUQGCATWUF4XFYPDJI5ORV";
            Message coolsms = new Message(api_key, api_secret);

            // 4 params(to, from, type, text) are mandatory. must be filled
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to", phone);
            params.put("from", "01024169485");
            params.put("type", "SMS");
            params.put("text", "밋코더 앱에서 " + userRepository.findUserById(userid).get(0).getUsername() + "님이 당신을 초대했습니다이 앱을 설치하여 초대장을 이용해보세요!");
            params.put("app_version", "test app 1.2"); // application name and version

            try {
               JSONObject obj = (JSONObject) coolsms.send(params);
               System.out.println(obj.toString());
            } catch (CoolsmsException e) {
               System.out.println(e.getMessage());
               System.out.println(e.getCode());
            }

            count[0]++;
         }
      });



      System.out.println( isFriendsUser );
      System.out.println(nothipynphonelist);

      HashMap< String, List<Long> > returnlist = new HashMap<>();
      returnlist.put("friendsIdList", friendsIdList );
      returnlist.put("isFriendsUser", isFriendsUser);



      return returnlist;
   }



}
