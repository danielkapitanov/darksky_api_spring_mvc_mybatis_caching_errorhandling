package darkskyapi.services;

import darkskyapi.exceptions.DarkSkyApiException;
import darkskyapi.exceptions.DatabaseException;
import darkskyapi.mappers.DarkSkyMapper;
import darkskyapi.model.Average;
import darkskyapi.model.DarkSky;
import darkskyapi.model.DarkSky2;
import darkskyapi.model.Forecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;




/**
 * Created by daniel on 26.07.17.
 */
@Service
public class DarkSkyService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DarkSkyMapper darkSkyMapper;

    public long dateInSecs(String date) {
        String[] dateArr = date.split("/");
        long months = (Integer.parseInt(dateArr[0]) - 1) * 2629746;
        long days = (Integer.parseInt(dateArr[1]) - 1) * 86400;
        long years = (Integer.parseInt(dateArr[2]) - 1970) * 31556952;

        return months + days + years;
    }

    public ArrayList<DarkSky2> datesTo2016(double lati, double longi, String date) {
        String[] dateArr = date.split("/");
        int years = (Integer.parseInt(dateArr[2]));
        ArrayList<DarkSky2> temp = new ArrayList<>();
        for (int i = 0; i <= 2016 - years; i++) {
            DarkSky2 d2 = new DarkSky2();
            int count = years + i;
            d2.setDate(dateArr[0] + "/" + dateArr[1] + "/" + count);

            long dateMillis = dateInSecs(date) + i * 31556952;

            DarkSky darkSky = restTemplate.getForObject("https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/"
                    + lati + "," + longi + "," + dateMillis + "?exclude=currently,flags", DarkSky.class);

            d2.setSummary(darkSky.getDaily().getData()[0].getSummary());
            long sunrise = darkSky.getDaily().getData()[0].getSunriseTime();
            long sunset = darkSky.getDaily().getData()[0].getSunsetTime();

            // Get the current second within the minute within the hour
            long sunriseSecond = sunrise % 60;
            long sunsetSecond = sunset % 60;

            // Get total minutes
            long totalMinutesr = sunrise / 60;
            long totalMinutess = sunset / 60;


            // Get the current minute in the hour
            long sunriseMinute = totalMinutesr % 60;
            long sunsetMinute = totalMinutess % 60;

            // Get the total hours
            long sunriseHours = totalMinutesr / 60;
            long sunsetHours = totalMinutess / 60;

            // Get the current hour
            long sunriseHour = sunriseHours % 24;
            long sunsetHour = sunsetHours % 24;

            d2.setSunriseTime(dateArr[0] + "/" + dateArr[1] + "/" + count + " " + sunriseHour + ":" + sunriseMinute);
            d2.setSunsetTime(dateArr[0] + "/" + dateArr[1] + "/" + count + " " + sunsetHour + ":" + sunsetMinute);
            d2.setPrecipProbability(darkSky.getDaily().getData()[0].getPrecipProbability());
            d2.setTemperatureMax(darkSky.getDaily().getData()[0].getTemperatureMax());
            d2.setWindSpeed(darkSky.getDaily().getData()[0].getWindSpeed());

            temp.add(d2);
        }
        return temp;
    }

    public Average avgHourly(double lati, double longi) {

        DarkSky darkSky = restTemplate.getForObject(
                "https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/" + lati + "," + longi + "/", DarkSky.class);

        Average avg = new Average();

        for (int i = 0; i < darkSky.getHourly().getData().length; i++) {
            avg.setCloudCover(avg.getCloudCover() + darkSky.getHourly().getData()[i].getCloudCover());
            avg.setDewPoint(avg.getDewPoint() + darkSky.getHourly().getData()[i].getDewPoint());
            avg.setHumidity(avg.getHumidity() + darkSky.getHourly().getData()[i].getHumidity());
            avg.setOzone(avg.getOzone() + darkSky.getHourly().getData()[i].getOzone());
            avg.setPressure(avg.getPressure() + darkSky.getHourly().getData()[i].getPressure());
            avg.setUvIndex(avg.getUvIndex() + darkSky.getHourly().getData()[i].getUvIndex());
            avg.setVisibility(avg.getVisibility() + darkSky.getHourly().getData()[i].getVisibility());
            avg.setWindBearing(avg.getWindBearing() + darkSky.getHourly().getData()[i].getWindBearing());
            avg.setWindGust(avg.getWindGust() + darkSky.getHourly().getData()[i].getWindGust());
            avg.setWindSpeed(avg.getWindSpeed() + darkSky.getHourly().getData()[i].getWindSpeed());
        }

        avg.setCloudCover(Math.round((avg.getCloudCover() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setDewPoint(Math.round((avg.getDewPoint() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setHumidity(Math.round((avg.getHumidity() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setOzone(Math.round((avg.getOzone() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setPressure(Math.round((avg.getPressure() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setUvIndex(avg.getUvIndex() / darkSky.getHourly().getData().length);
        avg.setVisibility(Math.round((avg.getVisibility() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setWindBearing(avg.getWindBearing() / darkSky.getHourly().getData().length);
        avg.setWindGust(Math.round((avg.getWindGust() / darkSky.getHourly().getData().length) * 100d) / 100d);
        avg.setWindSpeed(Math.round((avg.getWindSpeed() / darkSky.getHourly().getData().length) * 100d) / 100d);

        return avg;
    }

    @Cacheable("DarkSky")
    public Forecast[] getWForecast(double lati, double longi, GregorianCalendar c) throws DatabaseException, DarkSkyApiException{

        GregorianCalendar temp = c;
        System.out.println(c.getTimeInMillis());
        Forecast[] fc = new Forecast[8];


        try {
            ArrayList<Forecast> data = darkSkyMapper.getDatabase();
            if (data.get(0).getDate().equals(format(c, "MM/dd/yyyy"))) {
                fc[0] = data.get(7);
                fc[1] = data.get(6);
                fc[2] = data.get(5);
                fc[3] = data.get(4);
                fc[4] = data.get(3);
                fc[5] = data.get(2);
                fc[6] = data.get(1);
                fc[7] = data.get(0);
                System.out.println("got data from database");
                return fc;
            }
        } catch (Exception exception) {
            throw new DatabaseException();
        }

        try {

            DarkSky darkSky = restTemplate.getForObject(
                    "https://api.darksky.net/forecast/bc22b26533e2408b35a0f5fa86ec8efd/" + lati + "," + longi + "/", DarkSky.class);


//        for (int i = 0; i <= 7; i++) {
//            Forecast f = new Forecast();
//            c.setTimeInMillis(darkSky.getDaily().getData()[i].getTime() * 1000);
//            f.setDate(format(c, "MM/dd/yyyy"));
//            f.setSummary(darkSky.getDaily().getData()[i].getSummary());
//            c.setTimeInMillis(darkSky.getDaily().getData()[i].getSunriseTime() * 1000);
//            f.setSunrise(format(c, "HH:mm"));
//            c.setTimeInMillis(darkSky.getDaily().getData()[i].getSunsetTime() * 1000);
//            f.setSunset(format(c, "HH:mm"));
//            f.setPrecipProbability(darkSky.getDaily().getData()[i].getPrecipProbability());
//            f.setTemperatureMax(darkSky.getDaily().getData()[i].getTemperatureMax());
//            f.setWindSpeed(darkSky.getDaily().getData()[i].getWindSpeed());
//            f.setLatitude(lati);
//            f.setLongitude(longi);
//            fc[i] = f;
//            darkSkyMapper.addNew(f);
//
//        }
//        return fc;
//    }

            Forecast f = new Forecast();
            c.setTimeInMillis(darkSky.getDaily().getData()[7].getTime() * 1000);
            f.setDate(format(c, "MM/dd/yyyy"));
            f.setSummary(darkSky.getDaily().getData()[7].getSummary());
            c.setTimeInMillis(darkSky.getDaily().getData()[7].getSunriseTime() * 1000);
            f.setSunrise(format(c, "HH:mm"));
            c.setTimeInMillis(darkSky.getDaily().getData()[7].getSunsetTime() * 1000);
            f.setSunset(format(c, "HH:mm"));
            f.setPrecipProbability(darkSky.getDaily().getData()[7].getPrecipProbability());
            f.setTemperatureMax(darkSky.getDaily().getData()[7].getTemperatureMax());
            f.setWindSpeed(darkSky.getDaily().getData()[7].getWindSpeed());
            f.setLongitude(longi);
            f.setLatitude(lati);
//            fc[7] = f;
            darkSkyMapper.addNew(f);

            System.out.println("calling recursive method");

            return getWForecast(lati, longi, temp);
        } catch (Exception e){
            throw new DarkSkyApiException();
        }
    }

    public static String format(GregorianCalendar c, String pattern) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        fmt.setCalendar(c);
        String dateFormatted = fmt.format(c.getTime());
        return dateFormatted;

    }

}
