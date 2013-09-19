package samplejavafx.renderers.statuscheck;

import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import samplejavafx.model.statuscheck.StatusCheck;

public class StatusCheckRenderer {
    private final Circle circle;
    
    public StatusCheckRenderer(Circle circle) {
        this.circle = circle;
    }
    
    public void render(final StatusCheck statusCheck) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                circle.setFill(Paint.valueOf(statusCheck.status.getColorCode()));
            }
        });
    }
}
