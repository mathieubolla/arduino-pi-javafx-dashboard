package samplejavafx.model.meteo;

public class Meteo {
    public final Double currentConditionsTemperature;
    
    public final String currentConditionsIconURL;
    public final Double forecastCurrentDayMaximumTemperature;
    public final Double forecastCurrentDayMinimumTemperature;
    
    public final String forecastDayOneIconURL;
    public final Double forecastDayOneMaximumTemperature;
    public final Double forecastDayOneMinimumTemperature;
    
    public final String forecastDayTwoIconURL;
    public final Double forecastDayTwoMaximumTemperature;
    public final Double forecastDayTwoMinimumTemperature;
    
    public final String forecastDayThreeIconURL;
    public final Double forecastDayThreeMaximumTemperature;
    public final Double forecastDayThreeMinimumTemperature;

    public Meteo(Double currentConditionsTemperature, String currentConditionsIconURL, Double forecastCurrentDayMaximumTemperature, Double forecastCurrentDayMinimumTemperature, String forecastDayOneIconURL, Double forecastDayOneMaximumTemperature, Double forecastDayOneMinimumTemperature, String forecastDayTwoIconURL, Double forecastDayTwoMaximumTemperature, Double forecastDayTwoMinimumTemperature, String forecastDayThreeIconURL, Double forecastDayThreeMaximumTemperature, Double forecastDayThreeMinimumTemperature) {
        this.currentConditionsTemperature = currentConditionsTemperature;
        this.currentConditionsIconURL = currentConditionsIconURL;
        this.forecastCurrentDayMaximumTemperature = forecastCurrentDayMaximumTemperature;
        this.forecastCurrentDayMinimumTemperature = forecastCurrentDayMinimumTemperature;
        this.forecastDayOneIconURL = forecastDayOneIconURL;
        this.forecastDayOneMaximumTemperature = forecastDayOneMaximumTemperature;
        this.forecastDayOneMinimumTemperature = forecastDayOneMinimumTemperature;
        this.forecastDayTwoIconURL = forecastDayTwoIconURL;
        this.forecastDayTwoMaximumTemperature = forecastDayTwoMaximumTemperature;
        this.forecastDayTwoMinimumTemperature = forecastDayTwoMinimumTemperature;
        this.forecastDayThreeIconURL = forecastDayThreeIconURL;
        this.forecastDayThreeMaximumTemperature = forecastDayThreeMaximumTemperature;
        this.forecastDayThreeMinimumTemperature = forecastDayThreeMinimumTemperature;
    }
}