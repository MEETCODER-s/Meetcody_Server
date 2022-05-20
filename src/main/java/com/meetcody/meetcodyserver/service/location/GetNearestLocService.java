package com.meetcody.meetcodyserver.service.location;

import com.meetcody.meetcodyserver.domain.location.dao.StationXYDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GetNearestLocService {

    StationXYDAO stationXYDAO = new StationXYDAO();
    //    static final String HOST_URL = "https://meetcoder.s3.ap-northeast-2.amazonaws.com/station_coordinate.json";


    public Map<String, Double> getNearestStation(double input_lat, double input_lng){
        LinkedHashMap<String,Double> result  = new LinkedHashMap<String,Double>();
        HashMap<String,Double> distance1=new HashMap<String,Double>();
        HashMap<String, Object> map = new HashMap<>();

        try {
            //aws에서 역좌표를 가져오는 경우
//            String value=stationXYDAO.get_data_from_URL(HOST_URL);
            String value = readFileToString("static/station_coordinate.json");

            JSONArray jsonarray = new JSONArray(value);

            JSONObject jsonobject=null;

            String noDataExistStation="";
            int count=0;//위도경도 정보가 없는 역 개수 세기

            System.out.println(input_lat+", "+input_lng);

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
                }
                catch (Exception e){
                    System.out.print("");
                }
                List<String> letters = new ArrayList<String>();



                double t1=100*(input_lat-lat);
                double t2=100*(input_lng-lng);

                double list[]={t1,t2};


                if(t1>2000){
//                    System.out.println(name+"은 위도경도정보가 없습니다.");
                    noDataExistStation=noDataExistStation+name+", ";
                    count++;
                }
                else {

                    map.put(name, list);
                    double t3=t1*t1;
                    double t4=t2*t2;
                    double t5=t3+t4;
                    double t6=Math.sqrt(t5);
                    distance1.put(name,t6);


                }

            }


            System.out.println("위도경도 정보가 없는 역 :"+noDataExistStation+"\n"+"위도경도 정보가 없는 역은 총 "+count+"개 입니다.");


        } catch (Exception e) {
            e.printStackTrace();
        }


        Map <String,Double> distance2= sortByValue(distance1);

        Set keySet = distance2.keySet();
        Iterator<String> iter = keySet.iterator();

        int count5=0;
        while(iter.hasNext()&count5<5) {

            String key = (String)iter.next();
            result.put(key,Math.round(distance2.get(key)*100000)/10000.0);
            count5++;
        }
        System.out.println("제일 가까운 역은 "+result+"입니다");


        return result;
    }


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> results = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            results.put(entry.getKey(), entry.getValue());
        }

        return results;
    }


    //    @Value("classpath:/static/subway_map.json")
//    Resource resourceFile1;
    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readFileToString(String path) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(path);
        return asString(resource);
    }




}
