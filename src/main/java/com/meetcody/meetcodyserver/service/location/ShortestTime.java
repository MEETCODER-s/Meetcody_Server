package com.meetcody.meetcodyserver.service.location;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

import static com.meetcody.meetcodyserver.service.location.GetNearestLocService.readFileToString;

public class ShortestTime {
    static LinkedHashMap<String, LinkedHashMap<Integer, String>> stationWithCode1 = new LinkedHashMap<>();
    static LinkedHashMap<String, LinkedHashMap<Integer, String>> stationWithCode2 = new LinkedHashMap<>();
    static LinkedHashMap<String, LinkedHashMap<Integer, String>> stationWithCode3 = new LinkedHashMap<>();

    static LinkedHashMap<String, List<String>> AllStationName = new LinkedHashMap();
    static List<String> stations = new LinkedList<>();

    static HashMap<String, HashMap<String,Double>> finalResult = new HashMap<>();

    static Map <String, Double> nearestStations =new HashMap<>();
    static LinkedList<String> input_location = new LinkedList<>();
    static List<Double> input_location_lat = new ArrayList<>();
    static List<Double> input_location_lng = new ArrayList<>();

    static int MAX_SIZE = 0;

    static Double MAX_VALUE = 100000.0;




//    public static void getShortestTime(Map <String, Double> getnearestStation, LinkedList<String> input_locations, double[] temp1, double[] temp2,double[] temp3){
    public static List<String> getShortestTime(Map <String, Double> getnearestStation, LinkedList<String> input_locations, List<Double> input_lat, List<Double> input_lng){
        nearestStations=getnearestStation;
        input_location=input_locations;

        input_location_lat =input_lat;
        input_location_lng=input_lng;
//        input_location_lat.add(temp1[0]);
//        input_location_lat.add(temp2[0]);
//        input_location_lat.add(temp3[0]);
//        input_location_lng.add(temp1[1]);
//        input_location_lng.add(temp2[1]);
//        input_location_lng.add(temp3[1]);

        String value = (readFileToString("static/subway_map.json"));

        JSONArray jsonarray = new JSONArray(value);

        JSONObject jsonobject=null;

        String noDataExistStation="";
        int count=0;//위도경도 정보가 없는 역 개수 세기



        for (int i = 0; i < jsonarray.length(); i++) {
            jsonobject = jsonarray.getJSONObject(i);
            String line = jsonobject.getString("노선명");
//            System.out.println(line);

            String name = jsonobject.getString("정거장구성");
            String allstations[] = name.split(",");

            LinkedHashMap<Integer, String> eachStation = new LinkedHashMap<>();

            String stationBefore = "";

            for(int j=0; j<allstations.length; j++){

                String tmp[] = allstations[j].split("-");
                if(tmp[1].substring(tmp[1].length()-1).equals("역"))
                    tmp[1]=tmp[1].substring(0,tmp[1].length()-1);
                eachStation.put(Integer.valueOf(tmp[0]), tmp[1]);



                if(AllStationName.containsKey(tmp[1]) && !stationBefore.equals("")){//링크드 리스트 키 중에 역이름이  있으면
                    List<String> tmp2 = AllStationName.get(tmp[1]);

                    AllStationName.remove(tmp[1]);
                    if(!tmp2.contains(stationBefore)) {
                        tmp2.add(stationBefore);//역을 넣음
                    }

                    AllStationName.put(tmp[1],tmp2);


                    //내 앞의 역의 키 값에도 나를 추가함.
//                    List<String> tmp3 =new ArrayList<>();
                    List<String> tmp3 = AllStationName.get(stationBefore);

                    AllStationName.remove(stationBefore);
                    if(!tmp3.contains(tmp[1])){
                        tmp3.add(tmp[1]);

                    }

                    AllStationName.put(stationBefore,tmp3);

                    stationBefore=tmp[1];

                }
                else if(AllStationName.containsKey(tmp[1]) && stationBefore.equals("")){//첫번째 역이고  // 이미 탐색 했었음(환승역)
                    stationBefore=tmp[1];
                }
                else if(!AllStationName.containsKey(tmp[1]) && !stationBefore.equals("")){// 첫번재 역이 아니고 // 첫번째로 탐색

                    //역을 넣어주고 내 이전 역을 값에 넣어줌
                    List<String> tmp2 =new ArrayList<String>();
                    tmp2.add(stationBefore);
                    AllStationName.put(tmp[1], tmp2);

                    //내 앞의 역의 키 값에도 나를 추가함.
//                    List<String> tmp3 =new ArrayList<>();
                    List<String> tmp3 =AllStationName.get(stationBefore);

                    AllStationName.remove(stationBefore);
                    if(!tmp3.contains(tmp[1])) {
                        tmp3.add(tmp[1]);
                    }

                    AllStationName.put(stationBefore,tmp3);

                    stationBefore=tmp[1];


                }
                else{// 이전역이 없다 //첫번째로 탐색 !AllStationName.containsKey(tmp[1]) && stationBefore.equals("")

                    AllStationName.put(tmp[1], new ArrayList<String>());
                    stationBefore=tmp[1];
                }

                if (j==(allstations.length-1)) {
                    if(stationWithCode1.containsKey(line)){
                        if(stationWithCode2.containsKey(line)){
                            stationWithCode3.put(line,eachStation);

                        }
                        else{ stationWithCode2.put(line, eachStation); }
                    }
                    else{
                        stationWithCode1.put(line, eachStation);
                    }
                }
                stationBefore =tmp[1];
            }
        }

        AllStationName.forEach((key, list )->{
            stations.add(key);
        } );


        System.out.println(AllStationName);
        System.out.println(stations);

        readCSVfile();

        return searchAlgo();





    }

    public static List<String> searchAlgo(){

        MAX_SIZE = stations.size();

//        System.out.println(nearestStations);
        List<String> MostNearestStation = new ArrayList<>();

        final int[] count = {0};
        nearestStations.forEach((station, km)->{
            count[0]++;
            MostNearestStation.add(station);
        });

        System.out.println(input_location+", "+MostNearestStation+", " + input_location_lng);

        GetNearestLocService getNearestLocService = new GetNearestLocService();

        HashMap<Integer, List<String>> MostNearestStationForInputs = new HashMap();

        for( int i = 0; i<input_location_lat.size(); i++){
            List <String> tmp = new ArrayList<>();
            getNearestLocService.getNearestStation(input_location_lat.get(i), input_location_lng.get(i)).forEach((station, km)->{
                tmp.add(station);
            });
            MostNearestStationForInputs.put(i+1,tmp);

        }

        System.out.println(MostNearestStationForInputs);

        List<String> StartStaion = new ArrayList<>();


        //입력한 주소에사 가장 가까운 역을 시작값으로 잡음
        MostNearestStationForInputs.forEach((starts, list)->{
            StartStaion.add(list.get(0));
        });

        String value = (readFileToString("static/nearDistance.json"));
        JSONObject nearDistance_object = new JSONObject(value);

        double[][] weight = new double[MAX_SIZE+1][MAX_SIZE+1];
        double[] distance = new double[MAX_SIZE+1];

        weight = init_weight(weight, nearDistance_object);

        for (int i =0; i< stations.size(); i++) {
            for (int j = 0; j < stations.size(); j++) {
//                if(weight[i][j]!=MAX_VALUE) System.out.print(weight[i][j]+" ");
            }
//            System.out.println();
        }

        System.out.println("======================================");
        List<Double> MaxTime = new ArrayList<>();

        for (int end =0; end<MostNearestStation.size(); end++) {
            MaxTime.add(end, 0.0);
            for(int start = 0; start<StartStaion.size(); start++){

                int[][] record = new int[MAX_SIZE][MAX_SIZE];
                record = init_record(record);

                boolean[] searched = new boolean[MAX_SIZE];

                distance = shortest_path_with_dij(weight, record, stations.indexOf(StartStaion.get(start)), distance, searched);

                System.out.print( StartStaion.get(start) +" ~ " + MostNearestStation.get(end) + ":" + distance[stations.indexOf(MostNearestStation.get(end))] + " ");

                if( MaxTime.get(end) < distance[stations.indexOf(MostNearestStation.get(end))] )
                    MaxTime.add(end, distance[stations.indexOf(MostNearestStation.get(end))] );

                for (int j = 0; j < MAX_SIZE; j++) {
                    if (record[stations.indexOf(MostNearestStation.get(end))][j] != MAX_VALUE) {
                        System.out.print(stations.get(record[stations.indexOf(MostNearestStation.get(end))][j]) + " ");//저장된 경로를 출력한다
                    }
                }
                System.out.println(MostNearestStation.get(end));
            }
            System.out.println("===================최대값:"+MaxTime.get(end)+"===================");

        }
        return MostNearestStation;
    }

    public static double[][]  init_weight(double[][] weight, JSONObject nearDistance_object){
        for (int i =0; i< stations.size(); i++) {
            nearDistance_object.getJSONObject(stations.get(i));
            Iterator x = nearDistance_object.getJSONObject(stations.get(i)).keys();

            while ((x.hasNext())){
                String key = (String) x.next();
                weight[i][stations.indexOf(key)] = Double.parseDouble(nearDistance_object.getJSONObject(stations.get(i)).get(key).toString());
            }

            for (int j = 0; j < stations.size(); j++) {
                if(weight[i][j] == 0){
                    weight[i][j]=MAX_VALUE;
                }
            }
            weight[i][i]=0;
        }
        return weight;
    }

    public static int[][]  init_record(int record[][]){
        for (int i =0; i< stations.size(); i++) {
            for (int j = 0; j < stations.size(); j++) {
                record[i][j]=100000;
            }
        }
        return record;
    }

    public static int choose(double[] distance, boolean[] searched){
        double min = Double.MAX_VALUE;
        int minpos;
        minpos=-1;
        for(int i=0; i<MAX_SIZE; i++){
            if(distance[i]<min && !searched[i]){
                min=distance[i];
                minpos=i;
            }
        }
        return minpos;
    }



    public static double[] shortest_path_with_dij(double weight[][], int path[][], int start, double distance[], boolean searched[]) {

        int n = MAX_SIZE;
        int u = 0, w, j;

        int[] check = new int[MAX_SIZE];//한 정점으로 가는 정점을 표시

        for(int i=0; i<n; i++){//초기화
            distance[i] = weight[start][i];
            searched[i] =false;
            check[i]=1;
            path[i][0] = start;
        }

        searched[start] =true;//시작 정점 방문 표시
        distance[start] = 0;

        for (int i = 0; i < n - 2; i++) {
            u = choose(distance, searched);
            searched[u] = true;
            for (w = 0; w < n; w++) {
                if (!searched[w]) {
                    if (distance[u] + weight[u][w] < distance[w]) {
                        if (i == 0) {//처음에는 인접한 정점에 연결
                            path[w][check[w]] = u; //갱신된 경로를 경로 배열에 저장
                            check[w]++;
                        } else {
                            for (j = 0; j < (check[u] + 1); j++) {//저장된 만큼 반복
                                path[w][j] = path[u][j]; //경로를 갱신
                                path[w][j + 1] = u; //끝부분에 자기자신을 저장
                                check[w]++;
                            }
                        }
                        distance[w] = distance[u] + weight[u][w];
                    }
                }
            }
        }
        return distance;

    }

    public static void readCSVfile(){


        //엑셀 파일 읽기

        stations.forEach( station -> {
            List<String> eachNearStation = AllStationName.get(station);

            HashMap<String, Double> tmp2 = new HashMap<>();

            System.out.print(eachNearStation);
            for (int t = 0; t < eachNearStation.size(); t++) {

                InputStream is = null;
                try {
                    is = new FileInputStream(new File("src/main/resources/static/fareTable.xlsx"));
                    Workbook workbook = StreamingReader.builder()
                            .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                            .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                            .open(is);

                    List<List<String>> l = new ArrayList<List<String>>(AllStationName.values());

                    for (Sheet sheet : workbook) { //sheet마다

//                        System.out.println(sheet.getSheetName());
                        for (Row r : sheet) {
                            int cell_count = 0;
                            int trues = 0;
                            double time = 0;
                            for (Cell c : r) {

                                cell_count++;

                                String x = c.getStringCellValue();
//                                    System.out.print(".");

                                if (c.getStringCellValue().contains("(")) {
                                    x = c.getStringCellValue().substring(0, c.getStringCellValue().indexOf("("));
                                }
//                                    if (x.equals("금천구청")) System.out.print(x + x.equals(eachNearStation.get(t)));

                                if (cell_count == 2) {
                                    if (station.equals("서울")) {
                                        if (x.equals(station + "역")) {
                                            trues++;
                                        }
                                    } else {
//                                        System.out.print(c.getStringCellValue()+ x + " ");
                                        if (x.equals(station)) {
                                            trues++;
//                                            System.out.print(x+ x.equals(station) + " ");
                                        }
                                    }
                                }
                                if (cell_count == 4) {

                                    if (eachNearStation.get(t).equals("서울")) {
                                        if (x.equals(eachNearStation.get(t) + "역")) {
                                            trues++;
                                        }
                                    } else {
                                        if (x.equals(eachNearStation.get(t))) {

                                            trues++;

                                        }
                                    }
                                }
                                if (cell_count == 5 && trues == 2) {
                                    time = c.getNumericCellValue();
                                    if (!tmp2.containsKey(eachNearStation.get(t)))
                                        tmp2.put(eachNearStation.get(t), time);
                                    else {
                                        if (tmp2.get(eachNearStation.get(t)) > time) {
                                            tmp2.remove(eachNearStation.get(t));
                                            tmp2.put(eachNearStation.get(t), time);
                                        }
                                    }
                                    System.out.print(eachNearStation.get(t) + time + " ");
                                }
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                finalResult.put(station, tmp2);

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(finalResult);
        });


        org.json.simple.JSONObject obj11 = new org.json.simple.JSONObject();
        finalResult.forEach((result,list) ->{
            obj11.put(result,list);
        });

        try {
            FileWriter file = new FileWriter("/Users/eugenehwang/Desktop/MeetcodyServer/src/main/resources/static/nearDistance.json");
            file.write(obj11.toJSONString()); file.flush(); file.close(); }
        catch (IOException e) { e.printStackTrace(); }

        System.out.println(finalResult);

    }
}
