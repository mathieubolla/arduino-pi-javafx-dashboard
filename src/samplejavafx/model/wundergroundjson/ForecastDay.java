package samplejavafx.model.wundergroundjson;

public class ForecastDay {
    public ForecastDay(String icon_url, int period, Range high, Range low) {
        this.icon_url = icon_url;
        this.period = period;
        this.high = high;
        this.low = low;
    }
    
    private String icon_url;
    private int period;
    private Range high;
    private Range low;

    public String getIcon_url() {
        return icon_url;
    }
    
    public int getPeriod() {
        return period;
    }
    
    public Range getHigh() {
        return high;
    }
    
    public Range getLow() {
        return low;
    }
}
