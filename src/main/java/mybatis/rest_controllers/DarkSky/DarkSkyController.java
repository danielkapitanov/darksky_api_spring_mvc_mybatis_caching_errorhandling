package mybatis.rest_controllers.DarkSky;

import mybatis.exceptions.DarkSkyApiException;
import mybatis.exceptions.DatabaseException;
import mybatis.exceptions.ErrorPojo;
import mybatis.model.DarkSky.Average;
import mybatis.model.DarkSky.DarkSky;
import mybatis.model.DarkSky.DarkSky2;
import mybatis.model.DarkSky.Forecast;
import mybatis.services.DarkSky.DarkSkyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by daniel on 26.07.17.
 */
@RestController
public class DarkSkyController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DarkSkyService darkSkyService;

    @RequestMapping(value="/weather", method = RequestMethod.GET)
    public DarkSky getWeather (@RequestParam(value="latitude")double lati,
                               @RequestParam(value="longitude")double longi) {
        DarkSky darkSky = restTemplate.getForObject(
                "https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/"+lati+","+longi+"/", DarkSky.class);
        return darkSky;
    }

    @RequestMapping(value="/weatherfordate", method = RequestMethod.GET)
    public DarkSky getWeather (@RequestParam(value="latitude")double lati,
                               @RequestParam(value="longitude")double longi,
                                @RequestParam(value="date")String date) {

        long dateMillis = darkSkyService.dateInSecs(date);
        String url = "https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/"+lati+","+longi+","+dateMillis+
                "?exclude=currently,flags";

        DarkSky darkSky = restTemplate.getForObject(url, DarkSky.class);
        return darkSky;
    }

    @RequestMapping(value="/weatherfordates", method = RequestMethod.GET)
    public ArrayList<DarkSky2> getWeather2 (@RequestParam(value="latitude")double lati,
                                    @RequestParam(value="longitude")double longi,
                                    @RequestParam(value="date")String date) {

        return darkSkyService.datesTo2016(lati, longi, date);
    }

    @RequestMapping(value="/weather/average", method = RequestMethod.GET)
    public Average getAvgWeather (@RequestParam(value="latitude")double lati,
                                  @RequestParam(value="longitude")double longi) {


        return darkSkyService.avgHourly(lati,longi);
    }

    @RequestMapping(value="/weather/forecast", method = RequestMethod.GET)
    public Forecast[] getWeatherForecast (@RequestParam(value="latitude")double lati,
                                        @RequestParam(value="longitude")double longi) throws DatabaseException, DarkSkyApiException {

        return darkSkyService.getWForecast(lati,longi);
    }

    @ExceptionHandler(DatabaseException.class)
    public ErrorPojo dbError() {
        ErrorPojo e = new ErrorPojo("Connection to database failed.", 500);
        return e;
    }

    @ExceptionHandler(DarkSkyApiException.class)
    public ErrorPojo apiError() {
        ErrorPojo e = new ErrorPojo("Retrieving data from DarkSky API failed.", 400);
        return e;
    }


}
