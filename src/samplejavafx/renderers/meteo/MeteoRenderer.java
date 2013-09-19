package samplejavafx.renderers.meteo;

import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import samplejavafx.model.meteo.Meteo;

public class MeteoRenderer {
    private final Label currentConditionsTemperature;
    
    private final ImageView currentConditionsIcon;
    private final Label forecastCurrentDayMaximumTemperature;
    private final Label forecastCurrentDayMinimumTemperature;
    
    private final ImageView forecastDayOneConditionsIcon;
    private final Label forecastDayOneMaximumTemperature;
    private final Label forecastDayOneMinimumTemperature;
    
    private final ImageView forecastDayTwoConditionsIcon;
    private final Label forecastDayTwoMaximumTemperature;
    private final Label forecastDayTwoMinimumTemperature;
    
    private final ImageView forecastDayThreeConditionsIcon;
    private final Label forecastDayThreeMaximumTemperature;
    private final Label forecastDayThreeMinimumTemperature;
    
    private final DecimalFormat format = new DecimalFormat("#");

    public MeteoRenderer(Label currentConditionsTemperature, ImageView currentConditionsIcon, Label forecastCurrentDayMaximumTemperature, Label forecastCurrentDayMinimumTemperature, ImageView forecastDayOneConditionsIcon, Label forecastDayOneMaximumTemperature, Label forecastDayOneMinimumTemperature, ImageView forecastDayTwoConditionsIcon, Label forecastDayTwoMaximumTemperature, Label forecastDayTwoMinimumTemperature, ImageView forecastDayThreeConditionsIcon, Label forecastDayThreeMaximumTemperature, Label forecastDayThreeMinimumTemperature) {
        this.currentConditionsTemperature = currentConditionsTemperature;
        this.currentConditionsIcon = currentConditionsIcon;
        this.forecastCurrentDayMaximumTemperature = forecastCurrentDayMaximumTemperature;
        this.forecastCurrentDayMinimumTemperature = forecastCurrentDayMinimumTemperature;
        this.forecastDayOneConditionsIcon = forecastDayOneConditionsIcon;
        this.forecastDayOneMaximumTemperature = forecastDayOneMaximumTemperature;
        this.forecastDayOneMinimumTemperature = forecastDayOneMinimumTemperature;
        this.forecastDayTwoConditionsIcon = forecastDayTwoConditionsIcon;
        this.forecastDayTwoMaximumTemperature = forecastDayTwoMaximumTemperature;
        this.forecastDayTwoMinimumTemperature = forecastDayTwoMinimumTemperature;
        this.forecastDayThreeConditionsIcon = forecastDayThreeConditionsIcon;
        this.forecastDayThreeMaximumTemperature = forecastDayThreeMaximumTemperature;
        this.forecastDayThreeMinimumTemperature = forecastDayThreeMinimumTemperature;
    }
    
    public void render(final Meteo meteo) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentConditionsTemperature.setText(meteo.currentConditionsTemperature + "°C");
                
                currentConditionsIcon.setImage(new Image(meteo.currentConditionsIconURL));
                forecastCurrentDayMaximumTemperature.setText(format.format(meteo.forecastCurrentDayMaximumTemperature) + "°C");
                forecastCurrentDayMinimumTemperature.setText(format.format(meteo.forecastCurrentDayMinimumTemperature) + "°C");

                forecastDayOneConditionsIcon.setImage(new Image(meteo.forecastDayOneIconURL));
                forecastDayOneMaximumTemperature.setText(format.format(meteo.forecastDayOneMaximumTemperature) + "°C");
                forecastDayOneMinimumTemperature.setText(format.format(meteo.forecastDayOneMinimumTemperature) + "°C");

                forecastDayTwoConditionsIcon.setImage(new Image(meteo.forecastDayTwoIconURL));
                forecastDayTwoMaximumTemperature.setText(format.format(meteo.forecastDayTwoMaximumTemperature) + "°C");
                forecastDayTwoMinimumTemperature.setText(format.format(meteo.forecastDayTwoMinimumTemperature) + "°C");

                forecastDayThreeConditionsIcon.setImage(new Image(meteo.forecastDayThreeIconURL));
                forecastDayThreeMaximumTemperature.setText(format.format(meteo.forecastDayThreeMaximumTemperature) + "°C");
                forecastDayThreeMinimumTemperature.setText(format.format(meteo.forecastDayThreeMinimumTemperature) + "°C");
            }
        });
    }
}
