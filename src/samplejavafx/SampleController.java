package samplejavafx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import samplejavafx.renderers.meteo.MeteoRenderer;
import samplejavafx.renderers.statuscheck.StatusCheckRenderer;
import samplejavafx.renderers.thermoduino.ThermoduinoRenderer;
import samplejavafx.renderers.time.TimeRenderer;
import samplejavafx.services.meteo.MeteoService;
import samplejavafx.services.shared.DataDownloader;
import samplejavafx.services.statuscheck.StatusCheckService;
import samplejavafx.services.thermoduino.ThermoduinoService;
import samplejavafx.services.time.TimeService;

public class SampleController implements Initializable {
    
    public static final String WundergroundApiKey = ""; // Use your own, available for free on wunderground.com up to 500 calls per day
    public static final String WundergroundApiLocation = "zmw:00000.61.07017";
    public static final String TimeApiFormat = "?%25d%2F%25m%2F%25Y%20%25Hh%25M";
    public static final String TimeApiTimezone = "CET";
    public static final String ResizrExpectedAnswer = "something";
    public static final String CommentrExpectedAnswer = "OK";
    public static final String ScorerExpectedAnswer = "OK";
    public static final String ResizrHealthCheck = "http://someservice.com/checkStatus"; // This is a well hidden secret. Put anything you want to monitor that says 'something' on http with status code 200 (i.e. : A txt file on Apache with 'something' string will do for testing...)
    public static final String CommentrHealthCheck = ""; // Idem, but says OK instead of something. You got it.
    public static final String ScorerHealthCheck = "";
    
    @FXML
    private ImageView current;
    @FXML
    private ImageView dayOne;
    @FXML
    private ImageView dayTwo;
    @FXML
    private ImageView dayThree;
    @FXML
    private Label labelCurrentTemp;
    @FXML
    private Label forecastMin1;
    @FXML
    private Label forecastMin2;
    @FXML
    private Label forecastMin3;
    @FXML
    private Label forecastMax1;
    @FXML
    private Label forecastMax2;
    @FXML
    private Label forecastMax3;
    @FXML
    private Label labelMaxTemp;
    @FXML
    private Label labelMinTemp;
    @FXML
    private Label labelDateTime;
    @FXML
    private Circle scorerStatus;
    @FXML
    private Circle resizrStatus;
    @FXML
    private Circle commentrStatus;
    @FXML
    private Label roomTemperature;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final ExecutorService execution = Executors.newCachedThreadPool();

        final DataDownloader dataDownloader = new DataDownloader();

        final MeteoService meteoService = new MeteoService(dataDownloader, WundergroundApiKey, WundergroundApiLocation);
        final MeteoRenderer meteoRenderer = new MeteoRenderer(labelCurrentTemp, current, labelMaxTemp, labelMinTemp, dayOne, forecastMax1, forecastMin1, dayTwo, forecastMax2, forecastMin2, dayThree, forecastMax3, forecastMin3);

        final TimeService timeService = new TimeService(dataDownloader, TimeApiFormat, TimeApiTimezone);
        final TimeRenderer timeRenderer = new TimeRenderer(labelDateTime);

        final StatusCheckService resizrStatusCheck = new StatusCheckService(dataDownloader, "Resizr", ResizrExpectedAnswer, ResizrHealthCheck);
        final StatusCheckService commentrStatusCheck = new StatusCheckService(dataDownloader, "Commentr", CommentrExpectedAnswer, CommentrHealthCheck);
        final StatusCheckService scorerStatusCheck = new StatusCheckService(dataDownloader, "Scorer", ScorerExpectedAnswer, ScorerHealthCheck);

        final StatusCheckRenderer resizrStatusRenderer = new StatusCheckRenderer(resizrStatus);
        final StatusCheckRenderer scorerStatusRenderer = new StatusCheckRenderer(scorerStatus);
        final StatusCheckRenderer commentrStatusRenderer = new StatusCheckRenderer(commentrStatus);

        final ThermoduinoService thermoService = new ThermoduinoService();
        final ThermoduinoRenderer thermoRenderer = new ThermoduinoRenderer(roomTemperature);

        // Every 30 min get meteo
        scheduleJob(30 * 60 * 1000, meteoUpdate(meteoRenderer, meteoService), execution);

        // Every 30s get time
        scheduleJob(30 * 1000, timeUpdate(timeRenderer, timeService), execution);

        // Every 1 min get status checks
        scheduleJob(60 * 1000, statusCheckUpdates(resizrStatusRenderer, resizrStatusCheck, commentrStatusRenderer, commentrStatusCheck, scorerStatusRenderer, scorerStatusCheck), execution);

        // Every 5s get room temp
        scheduleJob(5 * 1000, thermoUpdate(thermoRenderer, thermoService), execution);

    }

    private void scheduleJob(final long interval, final Runnable job, final ExecutorService execution) {
        execution.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    job.run();
                    Thread.sleep(interval);
                    scheduleJob(interval, job, execution);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private Runnable meteoUpdate(final MeteoRenderer meteoRenderer, final MeteoService meteoService) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    meteoRenderer.render(meteoService.doDownloadMeteoData());
                } catch (Exception e) {
                    System.err.println("Huh hoh, can't update meteo data, " + e.getMessage());
                }
            }
        };
    }

    private Runnable timeUpdate(final TimeRenderer timeRenderer, final TimeService timeService) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    timeRenderer.render(timeService.getTime());
                } catch (Exception e) {
                    System.err.println("Huh hoh, can't update time data, " + e.getMessage());
                }
            }
        };
    }

    private Runnable statusCheckUpdates(final StatusCheckRenderer resizrStatusRenderer, final StatusCheckService resizrStatusCheck, final StatusCheckRenderer commentrStatusRenderer, final StatusCheckService commentrStatusCheck, final StatusCheckRenderer scorerStatusRenderer, final StatusCheckService scorerStatusCheck) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    resizrStatusRenderer.render(resizrStatusCheck.check());
                    commentrStatusRenderer.render(commentrStatusCheck.check());
                    scorerStatusRenderer.render(scorerStatusCheck.check());
                } catch (Exception e) {
                    System.err.println("Huh hoh, can't update status check data, " + e.getMessage());
                }
            }
        };
    }

    private Runnable thermoUpdate(final ThermoduinoRenderer thermoRenderer, final ThermoduinoService thermoService) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    thermoRenderer.render(thermoService.getData());
                } catch (Exception e) {
                    System.err.println("Huh hoh, can't update thermo data, " + e.getMessage());
                }
            }
        };
    }
}
