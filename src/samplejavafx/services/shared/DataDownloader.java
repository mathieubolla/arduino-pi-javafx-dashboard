package samplejavafx.services.shared;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;

public class DataDownloader {
    public String downloadData(String path) {
        InputStream inputStream = null;
        try {
            inputStream = getUrl(path);
            return IOUtils.toString(inputStream);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private InputStream getUrl(String path) throws MalformedURLException, IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(7000);
        
        connection.connect();
        int responseCode = connection.getResponseCode();
        
        if (responseCode != 200) {
            throw new RuntimeException("Code is not 200, instead "+responseCode);
        }
        
        InputStream connectionStream = connection.getInputStream();
        return connectionStream;
    }
}
