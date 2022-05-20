package com.meetcody.meetcodyserver.service;


import com.meetcody.meetcodyserver.domain.calendar.CalendarRepository;
import com.meetcody.meetcodyserver.domain.calendar.GetOauthForCalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import com.google.api.client.json.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.meetcody.meetcodyserver.domain.user.Oauth;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private static String GOOGLE_CALENDAR_LIST_URL;

    @Transactional
    public String requestCalendarList() {

        RestTemplate restTemplate = new RestTemplate();
        GOOGLE_CALENDAR_LIST_URL = "https://www.googleapis.com/calendar/v3/users/me/calendarList";

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(GOOGLE_CALENDAR_LIST_URL, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("responseEntity = " + responseEntity);
            return responseEntity.getBody();
        }
        return "구글 캘린더 목록 요청 실패";
    }


    private final GetOauthForCalenderRepository getOauthForCalenderRepository;



    public void getAvailableDateList(String Accesstocken){

//    public void getAvailableDateList(){

//        List<Oauth> list_of_user_oauth = getOauthForCalenderRepository.getGoogleOauthById(user_id);
//        if(list_of_user_oauth.size()==0){
//            System.out.println("사용자 정보가 없음");
//        }
//        Oauth user_Google_oauth = list_of_user_oauth.get(0);
        String access_tocken =Accesstocken;

        System.out.println("1");
        HttpTransport httpTransport = new ApacheHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        GoogleCredential credentials = new GoogleCredential().setAccessToken(user_Google_oauth.getAccessToken()); //액세스 토큰 값 받아온거 여기 넣어야함!!!!!!
        GoogleCredential credentials = new GoogleCredential().setAccessToken(access_tocken);


        //받아온 액세스 토큰으로 이벤트 리스트 출력하기
        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName("meetcody").build();
//        .builder(transport, jsonFactory)
//                .setApplicationName("Wayk by Senti")
//                .setHttpRequestInitializer(
//                        new HttpRequestInitializer() {
//                            public void initialize(HttpRequest request)
//                                    throws IOException {
//                                request.getHeaders()
//                                        .setAuthorization(
//                                                GoogleHeaders
//                                                        .getGoogleLoginValue(authToken));
//                            }
//                        })
//                .setJsonHttpRequestInitializer(
//                        new JsonHttpRequestInitializer() {
//
//                            public void initialize(
//                                    JsonHttpRequest request)
//                                    throws IOException {
//                                CalendarRequest calendarRequest = (CalendarRequest) request;
//                                calendarRequest
//                                        .setKey(CalendarClientCredentials.KEY);
//                            }
//                        }).build();
//        https://stackoverflow.com/questions/11531081/bad-startmin-startmax-on-get-google-calendar-entries-google-api-java-client



        //사용자의 캘린더 리스트 받아오기

        final String[] pageToken = {null};
        List<String> calender_id_list = new ArrayList<>();
        do {
            CalendarList calendarList = null;
            try {
                calendarList = service.calendarList().list().setPageToken(pageToken[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                System.out.println(calendarListEntry.getSummary());
                calender_id_list.add(calendarListEntry.getId().toString());
            }
            pageToken[0] = calendarList.getNextPageToken();
        } while (pageToken[0] != null);



        //각 캘린더에서 일정 가져오기

        DateTime startTime = new DateTime(new Date(), TimeZone.getTimeZone("Asia/Seoul"));
        DateTime endTime = new DateTime(new Date(), TimeZone.getTimeZone("Asia/Seoul"));

        pageToken[0] = null;
        do {
            Events events = null;
            try {
                //이건 메인 캘린더만 가져오게됨. -> 사용시 forEach문은 빼야함
                events = service.events().list("primary").setPageToken(pageToken[0]).execute();


            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Event> items = events.getItems();
            for (Event event : items) {

                System.out.println(event.getSummary());
            }
            pageToken[0] = events.getNextPageToken();
        } while (pageToken[0] != null);



        calender_id_list.forEach(calender_id ->{
            pageToken[0] = null;
            do {
                Events events = null;
                //이건 메인 캘린더만 가져오게됨. -> 사용시 forEach문은 빼야함
//                    events = service.events().list("primary").setTimeMax(endTime).setTimeMin(startTime).setPageToken(pageToken[0]).execute();
                //이건 각각의 캘린더의 이벤트들을 가져옴
//                    events = service.events().list(calender_id).setTimeMax(endTime).setTimeMin(startTime).setPageToken(pageToken[0]).execute();
                List<Event> items = events.getItems();
                for (Event event : items) {

                    System.out.println(event.getSummary());
                }
                pageToken[0] = events.getNextPageToken();
            } while (pageToken[0] != null);
        });


    }

}
