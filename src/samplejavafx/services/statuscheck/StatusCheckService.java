package samplejavafx.services.statuscheck;

import samplejavafx.model.statuscheck.StatusCheck;
import samplejavafx.services.shared.DataDownloader;

public class StatusCheckService {
    private final DataDownloader dataDownloader;
    private final String name;
    private final String expectedSuccess;
    private final String endpoint;

    public StatusCheckService(DataDownloader dataDownloader, String name, String expectedSuccess, String endpoint) {
        this.dataDownloader = dataDownloader;
        this.name = name;
        this.expectedSuccess = expectedSuccess;
        this.endpoint = endpoint;
    }
    
    public StatusCheck check() {
        try {
            switch (dataDownloader.downloadData(endpoint)) {
                case "OK":
                    return new StatusCheck(name, StatusCheck.Status.Ok);
                default:
                    return new StatusCheck(name, StatusCheck.Status.Fail);
            }
        } catch (Exception e) {
            return new StatusCheck(name, StatusCheck.Status.Unknown);
        }
    }
}
