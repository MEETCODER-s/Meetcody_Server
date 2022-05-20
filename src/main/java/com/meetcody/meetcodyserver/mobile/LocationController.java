package com.meetcody.meetcodyserver.mobile;

import com.meetcody.meetcodyserver.domain.invitation.Invitation;
import com.meetcody.meetcodyserver.domain.invitation.InvitationRepository;
import com.meetcody.meetcodyserver.domain.location.dao.StationXYDAO;
import com.meetcody.meetcodyserver.service.location.ChangelocService;
import com.meetcody.meetcodyserver.service.location.GetNearestLocService;
import com.meetcody.meetcodyserver.service.location.ShortestTime;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.meetcody.meetcodyserver.service.location.GetNearestLocService.readFileToString;

@RequestMapping("/v1/location")
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class LocationController {

    private JdbcTemplate jdbcTemplate;

    StationXYDAO stationXYDAO=new StationXYDAO();
    ChangelocService changelocService =new ChangelocService();
    GetNearestLocService getNearestLocService = new GetNearestLocService();
    String input_location1="";
    String input_location2="";


    private final InvitationRepository invitationRepository;

    @ResponseBody
    @PostMapping(value = "/sendstartloc",consumes="application/json; charset=utf8")
    public String getStartLoc ( @RequestBody HashMap<String, List<String>> userinfo) {

        if(userinfo.get("nometter").get(0).equals("true")){
            invitationRepository.updateNometter( Long.valueOf(userinfo.get("meetid").get(0)) );
            return "success";
        }
        else {
            System.out.println(userinfo.get("startaddr").get(0));

            invitationRepository.updateStartlocationById(userinfo.get("startaddr").get(0), Long.valueOf(userinfo.get("meetid").get(0)));

            return "success";
        }

    }

    @ResponseBody
    @PostMapping(value = "/sendrecomdloc",consumes="application/json; charset=utf8")
    public List<Double> sendRecomLoc( @RequestBody HashMap<String, List<String>> userinfo) {

        List<Double> latlng =new ArrayList<>();

        LinkedHashMap<String,Double> result  = new LinkedHashMap<String,Double>();
        HashMap<String,Double> distance1=new HashMap<String,Double>();
        HashMap<String, Object> map = new HashMap<>();

        try {
            String value = readFileToString("static/station_coordinate.json");

            JSONArray jsonarray = new JSONArray(value);

            JSONObject jsonobject=null;

            String noDataExistStation="";
            int count=0;//위도경도 정보가 없는 역 개수 세기

            for (int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                String line = jsonobject.getString("line");
                String name = jsonobject.getString("name");

                int code=0;
                double lat=0;
                double lng=0;
                try{
                    code= jsonobject.getInt("code");
                    lat = jsonobject.getDouble("lat");
                    lng = jsonobject.getDouble("lng");
                    name=jsonobject.getString("name");
                    if(name.equals(userinfo.get("recomLoc").get(0))){
                        latlng.add(lat);
                        latlng.add(lng);
                        return latlng;
                    }
                }
                catch (Exception e){
                    System.out.print("");
                }


                }


            System.out.println("위도경도 정보가 없는 역 :"+noDataExistStation+"\n"+"위도경도 정보가 없는 역은 총 "+count+"개 입니다.");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return latlng;

    }


    @ResponseBody
    @PostMapping(value = "/recommedloc",consumes="application/json; charset=utf8")
    public List<String> sendRecommededStation ( @RequestBody HashMap<String, List<String>> userinfo) {
        Invitation tmpinvitation = invitationRepository.findAllById(Long.valueOf(userinfo.get("meetid").get(0))).get(0);
        List<Invitation> allOtherInvitation = invitationRepository.findAllByTimeIdAndStatusEqualsACCEPT(tmpinvitation.getTime().getId());

        LinkedList<String> input_location = new LinkedList<>();
        LinkedList<double[]> getAllLoclatlng =new LinkedList<>();

        List<Double> input_lats = new ArrayList<>();
        List<Double> input_lngs = new ArrayList<>();


        final int[] index = {0};
        allOtherInvitation.forEach((invitation) -> {

            if(invitation.getStartLocation()!=null) {
                input_location.add(invitation.getStartLocation());

                getAllLoclatlng.add(changelocService.getGeoDataByAddrKakao(invitation.getStartLocation())); //[lat,lng]
                input_lats.add(getAllLoclatlng.get(index[0])[0]);
                input_lngs.add(getAllLoclatlng.get(index[0])[1]);
                System.out.println("입력된 주소의 위도 경도:" + getAllLoclatlng.get(index[0]));
                index[0]++;
            }
        });

        final Double[] lat = {new Double(0)};
        final Double[] lng = {new Double(0)};
        getAllLoclatlng.forEach(latlng->{
            lat[0] = lat[0] + latlng[0];
            lng[0] = lng[0] + latlng[1];
        });
        lat[0] = lat[0]/getAllLoclatlng.size();
        lng[0] = lng[0]/getAllLoclatlng.size();

        double[] center_loc_latlng = {lat[0], lng[0]};

        String center_add =changelocService.getReverseGeoByKakao(center_loc_latlng);

        System.out.print("중앙 지점: "+center_add +" ("+center_loc_latlng[0]+ ", "+center_loc_latlng[1]+")");

        if(center_loc_latlng[0]==0){
            System.out.println("입력값으로 받아진 위도 경도 값이 없습니다. api 상태나 위치 입력값이 올바른지 확인해주세요(와이파이가 지정된 api의 ip주소여야함)");
        }

        Map <String, Double> nearestStation=getNearestLocService.getNearestStation(center_loc_latlng[0], center_loc_latlng[1]);

        stationXYDAO.frontget();
        List<String>  RecommededStations = ShortestTime.getShortestTime(nearestStation, input_location, input_lats, input_lngs);

        System.out.println(RecommededStations);

        return RecommededStations;
    }

}

