package darkskyapi.rest_controllers;

import darkskyapi.exceptions.DarkSkyApiException;
import darkskyapi.exceptions.DatabaseException;
import darkskyapi.exceptions.ErrorPojo;
import darkskyapi.exceptions.InvalidKeyException;
import darkskyapi.model.Average;
import darkskyapi.model.DarkSky;
import darkskyapi.model.DarkSky2;
import darkskyapi.model.Forecast;
import darkskyapi.services.ApiKeyOperations;
import darkskyapi.services.DarkSkyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by daniel on 26.07.17.
 */
@RestController
public class DarkSkyController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DarkSkyService darkSkyService;
    @Autowired
    ApiKeyOperations apiKeyOperations;

    @RequestMapping(value="/weather/forplace/{apikey}", method = RequestMethod.GET)
    public DarkSky getWeather (@PathVariable(value = "apikey") String apikey,
                                @RequestParam(value="latitude")double lati,
                               @RequestParam(value="longitude")double longi) throws InvalidKeyException {
        if(apiKeyOperations.isValid(apikey)) {
            DarkSky darkSky = restTemplate.getForObject(
                    "https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/" + lati + "," + longi + "/", DarkSky.class);
            return darkSky;
        } else {
            throw new InvalidKeyException();
        }
    }

    @RequestMapping(value="/weather/fordate", method = RequestMethod.GET)
    public DarkSky getWeather (@RequestParam(value="latitude")double lati,
                               @RequestParam(value="longitude")double longi,
                                @RequestParam(value="date")String date) {

        long dateMillis = darkSkyService.dateInSecs(date);
        String url = "https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/"+lati+","+longi+","+dateMillis+
                "?exclude=currently,flags";

        DarkSky darkSky = restTemplate.getForObject(url, DarkSky.class);
        return darkSky;
    }

    @RequestMapping(value="/weather/fordates", method = RequestMethod.GET)
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

        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(c.getTimeInMillis() + 86400000 * 6);
        return darkSkyService.getWForecast(lati,longi, c);
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

    @ExceptionHandler(InvalidKeyException.class)
    public ErrorPojo invalidKeyError() {
        ErrorPojo e = new ErrorPojo("Invalid API key", 401);
        return e;
    }


}
