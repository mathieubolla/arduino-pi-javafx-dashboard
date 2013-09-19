package samplejavafx.services.meteo;

import com.google.gson.Gson;
import samplejavafx.model.wundergroundjson.AnswerConditions;
import samplejavafx.model.wundergroundjson.AnswerForecast;
import samplejavafx.model.meteo.Meteo;
import samplejavafx.services.shared.DataDownloader;

public class MeteoService {
    private final DataDownloader downloader;
    private final String wundergroundApiKey;
    private final String location;
    
    public MeteoService(DataDownloader downloader, String wundergroundApiKey, final String location) {
        this.location = location;
        this.wundergroundApiKey = wundergroundApiKey;
        this.downloader = downloader;
    }
    
    public Meteo doDownloadMeteoData() {
        String conditionsJson = downloader.downloadData("http://api.wunderground.com/api/" + wundergroundApiKey + "/" + "conditions" + "/q/"+location+".json");
        String forecastJson = downloader.downloadData("http://api.wunderground.com/api/" + wundergroundApiKey + "/" + "forecast" + "/q/"+location+".json");
        
        Gson gson = new Gson();
        AnswerForecast forecast = gson.fromJson(forecastJson, AnswerForecast.class);
        AnswerConditions conditions = gson.fromJson(conditionsJson, AnswerConditions.class);
        
        return new Meteo(
                conditions.getConditions().getCurrentTemperature(), 
                conditions.getConditions().getCurrentConditionsIconURL(), getForecastMax(forecast, 1), getForecastMin(forecast, 1),
                getForecastIcon(forecast, 2), getForecastMax(forecast, 2), getForecastMin(forecast, 2),
                getForecastIcon(forecast, 3), getForecastMax(forecast, 3), getForecastMin(forecast, 3),
                getForecastIcon(forecast, 4), getForecastMax(forecast, 4), getForecastMin(forecast, 4));
    }
    
    private String getForecastIcon(AnswerForecast answer, int index) {
        return answer.getForecast().getSimpleforecast().getForecastday(index).getIcon_url();
    }
    
    private double getForecastMin(AnswerForecast answer, int index) {
        return answer.getForecast().getSimpleforecast().getForecastday(index).getLow().getCelsius();
    }

    private double getForecastMax(AnswerForecast answer, int index) {
        return answer.getForecast().getSimpleforecast().getForecastday(index).getHigh().getCelsius();
    }
}
