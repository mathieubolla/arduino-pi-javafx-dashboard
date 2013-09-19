package samplejavafx.model.wundergroundjson;

import java.util.List;

public class Simpleforecast {
    public Simpleforecast(List<ForecastDay> forecastday) {
        this.forecastday = forecastday;
    }
    
    private List<ForecastDay> forecastday;

    public List<ForecastDay> getForecastday() {
        return forecastday;
    }
    
    public ForecastDay getForecastday(int period) {
        for (ForecastDay day : forecastday) {
            if (day.getPeriod() == period) {
                return day;
            }
        }
        
        throw new IllegalStateException("Huh hoh, couldn't find that day: "+period);
    }
}
