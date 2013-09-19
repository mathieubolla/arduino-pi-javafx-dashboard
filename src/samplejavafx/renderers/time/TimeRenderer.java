package samplejavafx.renderers.time;

import javafx.application.Platform;
import javafx.scene.control.Label;
import samplejavafx.model.time.Time;

public class TimeRenderer {
    private final Label timeLabel;
    
    public TimeRenderer(Label timeLabel) {
        this.timeLabel = timeLabel;
    }
    
    public void render(final Time time) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timeLabel.setText(time.timeString);
            }
        });
    }
}
