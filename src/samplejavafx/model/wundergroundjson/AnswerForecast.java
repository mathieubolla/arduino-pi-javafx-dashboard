package samplejavafx.model.wundergroundjson;

public class AnswerForecast {
    public AnswerForecast(Response response, Forecast forecast) {
        this.response = response;
        this.forecast = forecast;
    }
    
    private Response response;
    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public Response getResponse() {
        return response;
    }
}
