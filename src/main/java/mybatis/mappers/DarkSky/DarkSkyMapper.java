package mybatis.mappers.DarkSky;

import mybatis.model.DarkSky.Forecast;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

/**
 * Created by daniel on 27.07.17.
 */
@Mapper
public interface DarkSkyMapper {

    String ADD_NEW = "INSERT INTO `forecast`.Forecasts (date, summary, sunrise, sunset, precipProbability, temperatureMax, windSpeed, latitude, longitude) " +
            "values (#{date}, #{summary} , #{sunrise}, #{sunset}, #{precipProbability}, #{temperatureMax}, #{windSpeed}, #{latitude}, #{longitude})";
    String GET_LAST_8 = "SELECT * FROM `forecast`.Forecasts ORDER BY id desc LIMIT 8";

    @Insert(ADD_NEW)
    public void addNew(Forecast f);

    @Select(GET_LAST_8)
    public ArrayList<Forecast> getDatabase();


}
