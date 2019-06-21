package smart.league.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchService {

    private static final Logger LOGGER = Logger.getLogger(FetchService.class.getName());

    public JsonNode getData(String url){
        RestTemplate restTemplate = new RestTemplate();
        URI resourceUrl = null;
        try {
            resourceUrl = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO,"URL=== " + String.valueOf(resourceUrl));
        JsonNode response = restTemplate.getForObject(resourceUrl, JsonNode.class);
        return response;
    }


    public void downloadFromURL(String urlString, File destination) {
        try {
            LOGGER.log(Level.INFO,"URL To Download=== " + String.valueOf(urlString));
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            LOGGER.log(Level.INFO,"DESTINATION ==" + destination);
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
