package darkskyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by daniel on 26.07.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hourly {
    String summary;
    String icon;
    Data[] data;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }
}
