package samplejavafx.renderers.thermoduino;

import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.scene.control.Label;
import samplejavafx.model.thermoduino.ThermoduinoData;

public class ThermoduinoRenderer {
    private final Label tempLabel;
    
    private final DecimalFormat format = new DecimalFormat("#.##");
    
    public ThermoduinoRenderer(Label tempLabel) {
        this.tempLabel = tempLabel;
    }
    
    public void render(final ThermoduinoData data) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tempLabel.setText(format.format(data.temperature) + "°C");
            }
        });
    }
}
