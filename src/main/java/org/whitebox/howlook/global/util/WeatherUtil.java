package org.whitebox.howlook.global.util;

import com.querydsl.core.Tuple;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Component
@Log4j2
public class WeatherUtil {

    @Value("${weather.service-key}")
    private String serviceKey; // 저장될 경로

    Long NX = 149L; /* X축 격자점 수 */
    Long NY = 253L; /* Y축 격자점 수 */

    public Object[] getWeather(double Latitude, double Longitude) throws IOException, ParseException
    {
        String date = getDate(0);
        String now_time = getTime();

        if(now_time == "-100")
        {
            now_time = "2300";
            date = getDate(1);
        }

        LatXLngY tmp = convertGRID_GPS(TO_GRID, Latitude, Longitude);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /* 20210628 == 21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(now_time, "UTF-8")); /* 0600 == 06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(String.valueOf((int)tmp.x), "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(String.valueOf((int)tmp.y), "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());

        log.info(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String result= sb.toString();

        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);
        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        // body 로 부터 items 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items");

        // items로 부터 itemlist 를 받기
        JSONArray parse_item = (JSONArray) parse_items.get("item");
        String category;
        JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용

        // 카테고리와 값만 받아오기
        String day="";
        String time="";

        for(int i = 0 ; i < 12; i++) {
            weather = (JSONObject) parse_item.get(i);
            Object fcstValue = weather.get("fcstValue");
            Object fcstDate = weather.get("fcstDate");
            Object fcstTime = weather.get("fcstTime");
            //double형으로 받고싶으면 아래내용 주석 해제
            //double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
            category = (String)weather.get("category");
            // 출력
            if(!day.equals(fcstDate.toString())) {
                day=fcstDate.toString();
            }
            if(!time.equals(fcstTime.toString())) {
                time=fcstTime.toString();
                System.out.println(day+"  "+time);
            }
            System.out.print("\tcategory : "+ category);
            System.out.print(", fcst_Value : "+ fcstValue);
            System.out.print(", fcstDate : "+ fcstDate);
            System.out.println(", fcstTime : "+ fcstTime);
        }

        Object[] values = new String[2];
        values[0] = (String)((JSONObject)parse_item.get(0)).get("fcstValue");
        values[1] = (String)((JSONObject)parse_item.get(5)).get("fcstValue");

        return values;
    }

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;

    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }

    class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }

    public String getDate(int minus)
    {
        // 현재 날짜 가져오기
        LocalDate now = LocalDate.now().minusDays(minus);

        // DateTimeFormatter를 사용하여 날짜 포맷 변경
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return  formattedDate;
    }

    public String getTime()
    {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // DateTimeFormatter를 사용하여 시간 포맷 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        String formattedTime = now.format(formatter);

        return  convertTime(formattedTime);
    }


    public String convertTime(String inputTime) {
        int[] arr = {23, 20, 17, 14, 11, 8, 5, 2 ,-1};
        int inputHour = Integer.parseInt(inputTime.substring(0, 2));
        int inputMinute = Integer.parseInt(inputTime.substring(2));

        boolean refresh = false;

        if(inputMinute > 10)
        {
            refresh = true;
        }

        for (int i = 0; i < arr.length; i++) {
            if ((!refresh && arr[i] < inputHour) || (refresh && arr[i] <= inputHour)) {
                inputHour = arr[i];
                break;
            }
        }

        String convertedHourStr = String.format("%02d", inputHour);
        return convertedHourStr + "00";
    }
}

