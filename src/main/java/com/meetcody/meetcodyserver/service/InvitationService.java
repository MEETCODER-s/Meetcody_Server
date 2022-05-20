package com.meetcody.meetcodyserver.service;

import com.meetcody.meetcodyserver.domain.friend.FriendRepository;
import com.meetcody.meetcodyserver.domain.invitation.Invitation;
import com.meetcody.meetcodyserver.domain.invitation.InvitationRepository;
import com.meetcody.meetcodyserver.domain.invitation.enums.*;
import com.meetcody.meetcodyserver.domain.location.Location;
import com.meetcody.meetcodyserver.domain.location.LocationRepository;
import com.meetcody.meetcodyserver.domain.time.Time;
import com.meetcody.meetcodyserver.domain.time.TimeRepository;
import com.meetcody.meetcodyserver.domain.user.User;
import com.meetcody.meetcodyserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final TimeRepository timeRepository;
    private final LocationRepository locationRepository;

    public void saveInvitation(HashMap<String, List<String>> userinfo, String email){
        List<String> createdMeetInfo = userinfo.get("meetInfo");

        //선호시간대 enum 변환
        TimeSlot timeSlot;
        switch(createdMeetInfo.get(5)){
            case "7":
                System.out.println(TimeSlot.DAWN.getTitle());
                timeSlot= TimeSlot.DAWN;
                break;
            case "8":
                System.out.println(TimeSlot.MORNING.getTitle());
                timeSlot=TimeSlot.MORNING;
                break;
            case "9":
                System.out.println(TimeSlot.AFTERNOON.getTitle());
                timeSlot=TimeSlot.AFTERNOON;
                break;
            default:
                System.out.println(TimeSlot.NIGHT.getTitle());
                timeSlot=TimeSlot.NIGHT;
                break;
        }

        // 온.오프라인 enum 변환 / 장소추천여부
        Boolean getLocationRecom = false;
        Form form = Form.ONLINE;
        if(createdMeetInfo.get(6).equals("2")){
            getLocationRecom=true;
            form=Form.OFFLINE;
        }
        if(createdMeetInfo.get(6).equals("3"))
            form=Form.OFFLINE;

        //offline && false => 3번 장소 직접 입력

        //초대장유효시간
        LocalDateTime validateTime =  ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        System.out.println(validateTime);
        switch (createdMeetInfo.get(7)){
            case "11":
                validateTime = validateTime.plusHours(1);
                break;
            case "12":
                validateTime = validateTime.plusHours(2);
                break;
            case "13":
                validateTime = validateTime.plusHours(3);
                break;
            case "14":
                validateTime = validateTime.plusHours(6);
                break;
            case "15":
                validateTime = validateTime.plusHours(12);
                break;
            case "16":
                validateTime = validateTime.plusDays(1);
                break;
            default:
                validateTime = validateTime.plusDays(2);
                System.out.println(validateTime);
                break;

        }

        int years = Integer.parseInt(createdMeetInfo.get(1).substring(0,4));
        int yeare = Integer.parseInt(createdMeetInfo.get(2).substring(0,4));
        int months = Integer.parseInt(createdMeetInfo.get(1).substring(5,7));
        int monthe = Integer.parseInt(createdMeetInfo.get(2).substring(5,7));
        int days = Integer.parseInt(createdMeetInfo.get(1).substring(8));
        int daye = Integer.parseInt(createdMeetInfo.get(2).substring(8));

        Time time = Time.builder()
                .startRange(LocalDateTime.of(years,months,days,0,0))
                .endRange(LocalDateTime.of(yeare,monthe,daye,0,0))
                .isAccepted(false)
                .durationHour(Integer.valueOf(createdMeetInfo.get(3)))
                .durationMinute(Integer.valueOf(createdMeetInfo.get(4)))
                .build();
        timeRepository.save(time);

        Location location ;

        if(createdMeetInfo.get(6).equals("3")){
            location = Location.builder().isAccepted(false).build();
        }
        else{
            location = Location.builder().isAccepted(false).build();
        }

        locationRepository.save(location);





        HashMap< String, List<Long> > returnlist = friendService.getContactsListConvertPhonenum(userinfo.get("friendNum"), Long.valueOf(userinfo.get("userid").get(0)));
        List<Long> friendsIdList = returnlist.get("friendsIdList"); // 초대하는 친구 중 회원가입된 친구의 유저아이디 목록
        List<Long> isFriendsUser = returnlist.get("isFriendsUser");


        System.out.println("!!!!!!!!"+userinfo.get("userid").get(0));

        Category category = Category.PRIVATE ;
        if(userinfo.get("business").equals(Category.BUSINESS.getTitle())){
            category=Category.BUSINESS;
        }

        List<User> users = userRepository.findUserById(Long.valueOf(userinfo.get("userid").get(0)));
        User user = new User();
        if(users.size()!=0) {
            user = users.get(0);

            //먼저 생성자에게 생성자 권한 부여를 위해 초대장을 먼저 생성한다.
            Invitation invitation = Invitation.builder()
                    .user(user)
                    .title(createdMeetInfo.get(0))
                    .isLocation(getLocationRecom)
                    .isTime(true)
                    .expired(validateTime)
                    .timeSlot(timeSlot)
                    .role(Role.INVITER)
                    .status(Status.NEEDACTION)
                    .category(category)
                    .form(form)
                    .time(time)
                    .location(location)
                    .created(LocalDateTime.now())
                    .totalInvitees(Long.valueOf(friendsIdList.size()+1))
                    .build();
            invitationRepository.save(invitation);
        }

        Form finalForm = form;
        Boolean finalGetLocationRecom = getLocationRecom;
        User finalUser = user;
        LocalDateTime finalValidateTime = validateTime;
        Category finalCategory = category;
        friendsIdList.forEach(friendId->{
            if(friendId != finalUser.getId()) {
                User friend = userRepository.findUserById(friendId).get(0);

                Invitation invitation = Invitation.builder()
                        .user(friend)
                        .title(createdMeetInfo.get(0))
                        .isLocation(finalGetLocationRecom)
                        .isTime(true)
                        .expired(finalValidateTime)
                        .timeSlot(timeSlot)
                        .role(Role.INVITEE)
                        .status(Status.NEEDACTION)
                        .category(finalCategory)
                        .form(finalForm)
                        .created(LocalDateTime.now())
                        .time(time)
                        .location(location)
                        .totalInvitees(Long.valueOf(friendsIdList.size()+1))
                        .build();
                invitationRepository.save(invitation);
            }
        });



    }


}
