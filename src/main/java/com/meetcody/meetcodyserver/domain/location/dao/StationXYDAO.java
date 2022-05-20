package com.meetcody.meetcodyserver.domain.location.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//역좌표들 가져오는 클래스
public class StationXYDAO {

    //aws에서 json 파일을 불러와 모든 역의 좌표 데이터 불러오기
    public String get_data_from_URL(String HOST_URL) {
        StringBuilder response = null;
        try {
            //1. URL 클래스 이용해서 오픈API의 URL 입력
            //이때 ServiceKey 파라미터에는 발급받은 인증키 값을 넣는다
            URL url = new URL(HOST_URL);

            //2. HttpURLConnection 클래스의 openConnection메소드 이용해서 URL에 실제 연결
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //3. HttpURLConnection의 GET함수 호출
            con.setRequestMethod("GET");

            //Status Code 받아옴
            int statusCode = con.getResponseCode();

            response = new StringBuilder();

            //Status Code값에 따라 다른 로직을 타도록 분기
            switch (statusCode) {
                case 200:
                    //4. 데이터 받아와서 출력
                    //- 이때, 데이터에 한글이 포함되어 있는 경우 "UTF-8"을 반드시 파라미터로 같이 전달해주어야 깨지지 않고 제대로 나타나다.
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
//                        System.out.println(inputLine);
                        response.append(inputLine + "\n");
                    }

                    in.close();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = response.toString();
        return value;
    }
    public void frontget(){
        StringBuilder response = null;
        try {
            //1. URL 클래스 이용해서 오픈API의 URL 입력
            //이때 ServiceKey 파라미터에는 발급받은 인증키 값을 넣는다
            URL url = new URL("http://localhost:8080/start_input");

            //2. HttpURLConnection 클래스의 openConnection메소드 이용해서 URL에 실제 연결
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //3. HttpURLConnection의 GET함수 호출
            con.setRequestMethod("GET");

            //Status Code 받아옴
            int statusCode = con.getResponseCode();

            response = new StringBuilder();

            //Status Code값에 따라 다른 로직을 타도록 분기
            switch (statusCode) {
                case 200:
                    //4. 데이터 받아와서 출력
                    //- 이때, 데이터에 한글이 포함되어 있는 경우 "UTF-8"을 반드시 파라미터로 같이 전달해주어야 깨지지 않고 제대로 나타나다.
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
//                        System.out.println(inputLine);
                        response.append(inputLine + "\n");
                    }

                    in.close();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = response.toString();
        System.out.println(value);

    }

    // // 저장된 파일로 데이터 가져오는 경우
//    public String get_station_from_local(){


//        String strJson="";
//        try {
//            FileInputStream ins = new FileInputStream("/Users/eugenehwang/MeetcodyServer/src/station_coordinate.json");   // ex) "c:\\\\data.json"
//            InputStreamReader inr = new InputStreamReader(ins, "UTF-8");
//            BufferedReader br = new BufferedReader(inr);
//            strJson = br.readLine();
//        }catch (Exception e) {
//            System.out.print("");
//        }

//        JSONArray jsonarray = new JSONArray(strJson.toString());
//    }
}
