package com.meetcody.meetcodyserver.mobile;

import com.meetcody.meetcodyserver.domain.friend.FriendRepository;
import com.meetcody.meetcodyserver.domain.invitation.Invitation;
import com.meetcody.meetcodyserver.domain.invitation.InvitationRepository;
import com.meetcody.meetcodyserver.domain.invitation.enums.Status;
import com.meetcody.meetcodyserver.domain.location.LocationRepository;
import com.meetcody.meetcodyserver.domain.time.TimeRepository;
import com.meetcody.meetcodyserver.domain.user.UserRepository;
import com.meetcody.meetcodyserver.service.FriendService;
import com.meetcody.meetcodyserver.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/v1/invitation")
public class InvitationApiController {

    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final TimeRepository timeRepository;
    private final InvitationService invitationService;
    private final LocationRepository locationRepository;

    @ResponseBody
    @PostMapping(value = "/createMeeting",consumes="application/json; charset=utf8")
    public String createMeet ( @RequestBody HashMap<String, List<String>> userinfo) {

        System.out.println(userinfo.get("meetInfo"));
        //이름, 시작시간 , 종료시간, 약속 지속시간(시,분),선호시간대, 장소추천여, 초대장유효시간

        System.out.println(userinfo.get("friendNum"));
        System.out.println(userinfo.get("friendName"));
        String email =userinfo.get("email").get(0);
        System.out.println(email);

        invitationService.saveInvitation(userinfo, email);

        return "success";
    }

    @ResponseBody
    @PostMapping(value = "/meetlist",consumes="application/json; charset=utf8")
    public List<HashMap<String, String>>  postUserMeetList(@RequestBody HashMap<String, List<String>> userinfo) {
        System.out.println(userinfo.get("userid").get(0)+"유저아이디");

        List<Invitation> invitations =invitationRepository.findAllByUserId(Long.valueOf(userinfo.get("userid").get(0)));



        List<HashMap<String, String>> tofront = new ArrayList<>();
        final int[] count = {0};

        invitations.forEach(invitation -> {
            if( LocalDateTime.now().isBefore(invitation.getExpired()) && !invitation.getLocation().getIsAccepted() || invitation.getStatus().equals(Status.DECLINE) ) {
                count[0]++;
                HashMap<String, String> tmp = new HashMap<>();

                tmp.put("id", String.valueOf(invitation.getId()));
                tmp.put("title", invitation.getTitle());
                tmp.put("isLocation", String.valueOf(invitation.getIsLocation()));
                tmp.put("isTime", String.valueOf(invitation.getIsTime()));
                tmp.put("expireTimeMonth", String.valueOf(invitation.getExpired().getMonthValue()));
                tmp.put("expireTimeDate", String.valueOf(invitation.getExpired().getDayOfMonth()));
                tmp.put("expireTimeHour", String.valueOf(invitation.getExpired().getHour()));
                tmp.put("expireTimeMinute", String.valueOf(invitation.getExpired().getSecond()));
                tmp.put("timeSlot", invitation.getTimeSlot().getTitle());
                tmp.put("role", invitation.getRole().getTitle());
                tmp.put("category", invitation.getCategory().getTitle());
                tmp.put("form", invitation.getForm().getTitle());

                tmp.put("durationHour", String.valueOf(invitation.getTime().getDurationHour()));
                tmp.put("durationMinute", String.valueOf(invitation.getTime().getDurationMinute()));
                tmp.put("totalInvitees", String.valueOf(invitation.getTotalInvitees()));

                tmp.put("acceptInvitees", String.valueOf(invitationRepository.findAllByTimeIdACeepted( invitation.getTime().getId()).size()) );

                tmp.put("invitorName", invitationRepository.findByTimeIdAndRoleEqualsINVITER(invitation.getTime().getId()).getUser().getUsername());
                LocalDateTime starttime =invitation.getTime().getStartRange();
                tmp.put("startTimeYear", String.valueOf(starttime.getYear()));
                tmp.put("startTimeMonth", String.valueOf(starttime.getMonthValue()));
                tmp.put("startTimeDate", String.valueOf(starttime.getDayOfMonth()));
                LocalDateTime endtime = invitation.getTime().getEndRange();
                tmp.put("endTimeYear", String.valueOf(endtime.getYear()));
                tmp.put("endTimeMonth", String.valueOf(endtime.getMonthValue()));
                tmp.put("endTimeDate", String.valueOf(endtime.getDayOfMonth()));
                tmp.put("mystatus", String.valueOf(invitation.getStatus()));
                String mystartLoc = "";
                if(invitation.getStatus().equals(Status.ACCEPT)){

                    if(invitation.getStartLocation()!=null)
                        mystartLoc = String.valueOf(invitation.getStartLocation());

                    else
                        mystartLoc = "출발지는 상관없어요!";
                }
                tmp.put("myStartLoc",mystartLoc);


                tofront.add(tmp);

            }

        });


        return tofront;

    }
    @ResponseBody
    @PostMapping(value = "/acceptInvitation",consumes="application/json; charset=utf8")
    public void acceptInvitation(@RequestBody HashMap<String, List<String>> userinfo) {
        Invitation invitation = invitationRepository.findAllById(Long.valueOf(userinfo.get("meetid").get(0))).get(0);
//        Boolean true1 = new Boolean(true);
        invitationRepository.updateLocaionAceepted(invitation.getLocation().getId());
        invitationRepository.updateTimeAceepted(invitation.getTime().getId());

        List<String> recommedLoc = userinfo.get("recommedLoc");

        locationRepository.updateRecomLoc(recommedLoc.get(Integer.parseInt(userinfo.get("locationSelect").get(0))), invitation.getLocation().getId());



    }


    @ResponseBody
    @PostMapping(value = "/confirmedList",consumes="application/json; charset=utf8")
    public List<HashMap<String, String>>  postConfirmedList(@RequestBody HashMap<String, List<String>> userinfo) {



        List<Invitation> invitations =invitationRepository.findAllByUserId(Long.valueOf(userinfo.get("userid").get(0)));

        List<HashMap<String, String>> tofront = new ArrayList<>();
        final int[] count = {0};

        invitations.forEach(invitation -> {
            if( invitation.getLocation().getIsAccepted() ) {

                count[0]++;
                HashMap<String, String> tmp = new HashMap<>();

                tmp.put("id", String.valueOf(invitation.getId()));
                tmp.put("title", invitation.getTitle());
                tmp.put("isLocation", String.valueOf(invitation.getIsLocation()));
                tmp.put("isTime", String.valueOf(invitation.getIsTime()));
                tmp.put("expireTimeMonth", String.valueOf(invitation.getExpired().getMonthValue()));
                tmp.put("expireTimeDate", String.valueOf(invitation.getExpired().getDayOfMonth()));
                tmp.put("expireTimeHour", String.valueOf(invitation.getExpired().getHour()));
                tmp.put("expireTimeMinute", String.valueOf(invitation.getExpired().getSecond()));
                tmp.put("timeSlot", invitation.getTimeSlot().getTitle());
                tmp.put("role", invitation.getRole().getTitle());
                tmp.put("category", invitation.getCategory().getTitle());
                tmp.put("form", invitation.getForm().getTitle());

                tmp.put("durationHour", String.valueOf(invitation.getTime().getDurationHour()));
                tmp.put("durationMinute", String.valueOf(invitation.getTime().getDurationMinute()));
                tmp.put("totalInvitees", String.valueOf(invitation.getTotalInvitees()));

                tmp.put("acceptInvitees", String.valueOf(invitationRepository.findAllByTimeIdACeepted( invitation.getTime().getId()).size()));

                tmp.put("invitorName", invitationRepository.findByTimeIdAndRoleEqualsINVITER(invitation.getTime().getId()).getUser().getUsername());
                LocalDateTime starttime =invitation.getTime().getStartRange();
                tmp.put("startTimeYear", String.valueOf(starttime.getYear()));
                tmp.put("startTimeMonth", String.valueOf(starttime.getMonthValue()));
                tmp.put("startTimeDate", String.valueOf(starttime.getDayOfMonth()));
                LocalDateTime endtime = invitation.getTime().getEndRange();
                tmp.put("endTimeYear", String.valueOf(endtime.getYear()));
                tmp.put("endTimeMonth", String.valueOf(endtime.getMonthValue()));
                tmp.put("endTimeDate", String.valueOf(endtime.getDayOfMonth()));
                tmp.put("mystatus", String.valueOf(invitation.getStatus()));
                String mystartLoc = "";
                if(invitation.getStatus().equals(Status.ACCEPT)){

                    if(invitation.getStartLocation()!=null)
                        mystartLoc = String.valueOf(invitation.getStartLocation());

                    else
                        mystartLoc = "출발지는 상관없어요!";
                }
                tmp.put("myStartLoc",mystartLoc);

                tmp.put("recomLoc", invitation.getLocation().getRecomLocName());

                System.out.println(invitation.getTitle());


                tofront.add(tmp);

            }
            if(invitation.getExpired().isBefore(LocalDateTime.now()) ){

            }
        });


        return tofront;
    }

}
