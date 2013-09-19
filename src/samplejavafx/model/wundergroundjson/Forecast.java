package samplejavafx.model.wundergroundjson;

public class Forecast {

    public Forecast(Simpleforecast simpleforecast) {
        this.simpleforecast = simpleforecast;
    }
    
    private Simpleforecast simpleforecast;

    public Simpleforecast getSimpleforecast() {
        return simpleforecast;
    }
}
