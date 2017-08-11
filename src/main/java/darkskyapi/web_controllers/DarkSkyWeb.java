package darkskyapi.web_controllers;

import darkskyapi.exceptions.DarkSkyApiException;
import darkskyapi.exceptions.DatabaseException;
import darkskyapi.model.Forecast;
import darkskyapi.model.LatLong;
import darkskyapi.model.User;
import darkskyapi.services.DarkSkyService;
import darkskyapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.GregorianCalendar;

/**
 * Created by daniel on 03.08.17.
 */
@Controller
public class DarkSkyWeb {

    @Autowired
    DarkSkyService darkSkyService;

    @Autowired
    UserService userService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }


    @RequestMapping( value = "/forecast", method = RequestMethod.GET )
    public String forecastForm(Model model) {
        model.addAttribute("latlong", new LatLong());

        return "forecast";
    }


    @RequestMapping( value = "/forecast", method = RequestMethod.POST )
    public String forecastSubmit(Model model, LatLong latlong) throws DatabaseException, DarkSkyApiException {

        Forecast[] fc = new Forecast[8];
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(c.getTimeInMillis() + 86400000 * 7);
        fc = darkSkyService.getWForecast(latlong.getLatitude(), latlong.getLongitude(), c);

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
