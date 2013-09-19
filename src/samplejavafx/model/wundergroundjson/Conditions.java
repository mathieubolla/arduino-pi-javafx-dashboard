package samplejavafx.model.wundergroundjson;

public class Conditions {
    private final double temp_c;
    private final String icon_url;
    
    public Conditions(double temp_c, String icon_url) {
        this.temp_c = temp_c;
        this.icon_url = icon_url;
    }
    
    public double getCurrentTemperature() {
        return temp_c;
    }
    
    public String getCurrentConditionsIconURL() {
        return icon_url;
    }
}
