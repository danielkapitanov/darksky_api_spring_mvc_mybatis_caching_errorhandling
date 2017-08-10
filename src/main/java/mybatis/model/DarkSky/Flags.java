package mybatis.model.DarkSky;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by daniel on 26.07.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flags {
    String[] sources;
    @JsonProperty("isd-stations")
    String[] isd_stations;
    String units;

    public String[] getSources() {
        return sources;
    }

    public void setSources(String[] sources) {
        this.sources = sources;
    }

    public String[] getIsd_stations() {
        return isd_stations;
    }

    public void setIsd_stations(String[] isd_stations) {
        this.isd_stations = isd_stations;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
