package mybatis.web_controllers;

import mybatis.exceptions.DarkSkyApiException;
import mybatis.exceptions.DatabaseException;
import mybatis.model.DarkSky.Forecast;
import mybatis.model.DarkSky.LatLong;
import mybatis.services.DarkSky.DarkSkyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by daniel on 03.08.17.
 */
@Controller
public class DarkSkyWeb {

    @Autowired
    DarkSkyService darkSkyService;


    @RequestMapping( value = "/forecast", method = RequestMethod.GET )
    public String forecastForm(Model model) {
        model.addAttribute("latlong", new LatLong());

        return "forecast";
    }


    @RequestMapping( value = "/forecast", method = RequestMethod.POST )
    public String forecastSubmit(Model model, LatLong latlong) throws DatabaseException, DarkSkyApiException {

        Forecast[] fc = new Forecast[8];
        fc = darkSkyService.getWForecast(latlong.getLatitude(), latlong.getLongitude());

        model.addAttribute("fc", fc);

        return "result";
    }

    @ExceptionHandler(DatabaseException.class)
    public String dbError() {
        return "dberror";
    }

    @ExceptionHandler(DarkSkyApiException.class)
    public String apiError() {
        return "apierror";
    }
}
