package samplejavafx.services.time;

import samplejavafx.model.time.Time;
import samplejavafx.services.shared.DataDownloader;

public class TimeService {
    private final DataDownloader dataDownloader;
    private final String timeZone;
    private final String timeFormat;
    
    public TimeService(DataDownloader dataDownloader, final String timeFormat, final String timeZone) {
        this.timeFormat = timeFormat;
        this.timeZone = timeZone;
        this.dataDownloader = dataDownloader;
    }
    
    public Time getTime() {
        return new Time(dataDownloader.downloadData("http://www.timeapi.org/"+timeZone+"/now"+timeFormat));
    }
}
