package com.meetcody.meetcodyserver.service.location;

import com.mysql.cj.xdevapi.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;

public class ChangelocService {

    public static double[] getGeoDataByAddrGoogle(String completeAddress) {
        double[] loc = new double[2];
        try {

//            completeAddress="Sookmyung Women's+Univ., Cheongpa-dong+2-ga, Yongsan-gu, Seoul, Korea";
//            completeAddress="서울특별시 용산구 청파동2가 숙명여자대학교";//영문 한글 주소(기본주소 or 도로명 주소) 모두 가능

            completeAddress = completeAddress.replaceAll(" ", "+");
//            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="+completeAddress+"&key="+API_KEY;
            String surl = "";

            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;

            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            JSONObject jo = new JSONObject(responseStrBuilder.toString());
            JSONArray results = jo.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject jsonObject;
                jsonObject = results.getJSONObject(0);
                Double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                loc[0] = lat;
                loc[1] = lng;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(loc[0] + ", " + loc[1]);
        return loc;
    }

    //google api 사용시
    private static String API_KEY = "b96c84e8df884c1a7168d6aacb319676";

    public String get_reverse_add_google(double[] center_loc){

        System.out.println("center_add2"+center_loc[0]);
        String reverse_url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+center_loc[0]+","+center_loc[1]+"&key="+API_KEY;
        StringBuilder responseStrBuilder=null;
        try {
            URL url = new URL(reverse_url);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            responseStrBuilder = new StringBuilder();
            String inputStr;

            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
        }catch (Exception e){

        }

        JSONObject jo = new JSONObject(responseStrBuilder.toString());
        JSONArray results = jo.getJSONArray("results");

        String center_add="";
        if(results.length() > 0) {
            JSONObject jsonObject;
            jsonObject = results.getJSONObject(0);
            center_add=jsonObject.getString("formatted_address");
        }
        return center_add;
    }




    //naver api 사용시
    static final String clientSecret = "I6XzWVxKjJIzGbSeI4kkfTs0zxhRaw7KdNKM0SIy";  //clientSecret
    static final String clientId = "fcpwcvz4vb"; //clientId

    public static double[] getGeoDataByAddrNaver(String completeAddr){
        double[] loc = new double[2];
        StringBuffer response = new StringBuffer();

        try {
            String addr = URLEncoder.encode(completeAddr, "UTF-8");  //주소입력
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputStr;
            StringBuilder responseStrBuilder = new StringBuilder();


            while ((inputStr = br.readLine()) != null) {
                response.append(inputStr);
            }
            br.close();

            JsonParser parser=new JsonParser();
            JSONObject jo1 = new JSONObject(JsonParser.parseDoc(response.toString()).toString());
            JSONObject jo2 = new JSONObject(jo1.getJSONArray("addresses").get(0).toString());
            loc[0] = jo2.getDouble("y");
            loc[1] = jo2.getDouble("x");



        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(loc[0] + ", " + loc[1]);
        return loc;
    }

    //kakao api 사용시
    static final String adminKakao = "b96c84e8df884c1a7168d6aacb319676";

    public String getReverseGeoByKakao(double[] center_loc) {

        StringBuffer response = new StringBuffer();
        String reverse_url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+center_loc[1]+"&y="+center_loc[0];


        try {
            URL url = new URL(reverse_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK "+adminKakao);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputStr;
            StringBuilder responseStrBuilder = new StringBuilder();


            while ((inputStr = br.readLine()) != null) {
                response.append(inputStr);
            }
            br.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(response);

        JsonParser parser=new JsonParser();
        String result="";
        JSONObject jo1 = new JSONObject(JsonParser.parseDoc(response.toString()).toString());
        JSONObject jo2 = new JSONObject(jo1.getJSONArray("documents").get(0).toString());

        result=jo2.getJSONObject("address").get("address_name").toString();

        return result;
    }


    public double[] getGeoDataByAddrKakao(String completeAddr) {

        StringBuffer response = new StringBuffer();


        try {
            String reverse_url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + URLEncoder.encode(completeAddr, "UTF-8");

            URL url = new URL(reverse_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK "+adminKakao);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputStr;
            StringBuilder responseStrBuilder = new StringBuilder();


            while ((inputStr = br.readLine()) != null) {
                response.append(inputStr);
            }
            br.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);

        JsonParser parser=new JsonParser();

        JSONObject jo1 = new JSONObject(JsonParser.parseDoc(response.toString()).toString());
        JSONObject jo2 = new JSONObject(jo1.getJSONArray("documents").get(0).toString());

        double[] result={jo2.getDouble("y"),jo2.getDouble("x")};

        return result;
    }


}
