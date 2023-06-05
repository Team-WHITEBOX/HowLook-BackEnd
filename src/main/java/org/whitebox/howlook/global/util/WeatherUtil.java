package org.whitebox.howlook.global.util;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.whitebox.howlook.domain.post.dto.WeatherDTO;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.whitebox.howlook.global.error.ErrorCode.GPS_NOT_FOUND;

@Component
@Log4j2
public class WeatherUtil {

    @Value("${weather.service-key}")
    private String serviceKey;
    private static String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private static final Long NX = 149L;
    private static final Long NY = 253L;
    private static final int TO_GRID = 0;
    private static final int TO_GPS = 1;

    class LatXLngY
    {
        public double lat;
        public double lng;
        public double x;
        public double y;

    }

    public WeatherDTO getWeather(double latitude, double longitude) throws IOException, ParseException {
        // 기상청에서 제공하는 날씨정보가 3시간 단위로 10분마다 갱신되기 때문에 그에 맞는 시간으로 검색해야 함
        String currentDate = getDate(0);
        String currentTime = getTime();

        if (currentTime.equals("-100")) {
            currentTime = "2300";
            currentDate = getDate(1);
        }

        // 위도,경도가 아닌 기상청이 사용하는 격자 좌표로 검색해야함
        LatXLngY gridCoordinates = convertGRID_GPS(TO_GRID, latitude, longitude);
        String response = callWeatherApi(currentDate,currentTime,gridCoordinates);

        log.info("====");
        log.info(response);

        JSONArray weatherDataArray = parseWeatherArrayFromJson(response);
        String temperature = getWeatherValueByIndex(weatherDataArray, 0);
        String wind = getWeatherValueByIndex(weatherDataArray, 5);

        return new WeatherDTO(
                Long.parseLong(temperature),
                Long.parseLong(wind)
        );
    }

    private String callWeatherApi(String currentDate, String currentTime, LatXLngY gridCoordinates){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",serviceKey)
                        .queryParam("pageNo","1")
                        .queryParam("numOfRows","12")
                        .queryParam("dataType","JSON")
                        .queryParam("base_date",currentDate)
                        .queryParam("base_time",currentTime)
                        .queryParam("nx", String.valueOf((int) gridCoordinates.x))
                        .queryParam("ny",String.valueOf((int) gridCoordinates.y)).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private JSONArray parseWeatherArrayFromJson(String jsonResponse) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jsonResponse);

        JSONObject parseResponse = (JSONObject) obj.get("response");

        JSONObject parseHeader = (JSONObject) parseResponse.get("header");

        if(!parseHeader.get("resultCode").equals("00"))
            throw new EntityNotFoundException(GPS_NOT_FOUND);

        JSONObject parseBody = (JSONObject) parseResponse.get("body");
        JSONObject parseItems = (JSONObject) parseBody.get("items");

        return (JSONArray) parseItems.get("item");
    }

    private String getWeatherValueByIndex(JSONArray weatherDataArray, int index) {
        return (String) ((JSONObject) weatherDataArray.get(index)).get("fcstValue");
    }

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

