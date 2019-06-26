package smart.league.project.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.web.client.RestTemplate;
import smart.league.project.objects.Company;
import smart.league.project.objects.Items;
import smart.league.project.objects.Venues;
import smart.league.project.server.exchange.HttpExchange;
import smart.league.project.util.map.MultiValueMap;
import smart.league.project.util.web.json.JsonResponse;

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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchService {

    private static final Logger LOGGER = Logger.getLogger(FetchService.class.getName());
    private static final String CLIENT_ID = "PSFABU4KK2FU52OEFONQVBFG51CV0EJBKR33V4HPA4V0CXDO";
    private static final String CLIENT_SECRET = "NEYTUZLDHPYKGM22M4CK5Z1HXLEGRI2MNWECRIWHWC1HEM2U";
    private ObjectMapper mapper;


    public void downloadSource(MultiValueMap<String, String> queryParameters, Map<String, Object> body) {

        final String param = queryParameters.getFirst("type");

        switch (param){
            case "download":
                LOGGER.log(Level.INFO,"DOWNLOAD ");

                download(body.get("download").toString(), "");
                break;
            case "lookUp":
                break;
        }

    }

    private void download(String url, String localFile) {
    }

    public void getPics(HttpExchange exchange) {

        ObjectReader objectReader;
        String downloadPath = System.getProperty("user.home") + "/foursquare/";
        File foursquareDir = new File(downloadPath);
        if (!foursquareDir.exists()) {
            if (foursquareDir.mkdir()) {
                LOGGER.log(Level.INFO,"Directory " + downloadPath + "is created!");
            } else {
                LOGGER.log(Level.INFO,"Failed to create directory " + downloadPath + "!");
            }
        }


        //fetch h https://api.hitta.se/search/v7/app/combined/within/57.840703831916%3A11.728156448084002%2C57.66073920808401%3A11.908121071915998/?range.to=101&range.from=1&geo.hint=57.75072152%3A11.81813876&sort.order=relevance&query=lunch
        JsonNode h = getData("https://api.hitta.se/search/v7/app/combined/within/57.840703831916%3A11.728156448084002%2C57.66073920808401%3A11.908121071915998/?range.to=101&range.from=1&geo.hint=57.75072152%3A11.81813876&sort.order=relevance&query=lunch");


        //foreach c h.result.companies.company
        List<Company> companies = null;
        try {
            objectReader =  new ObjectMapper().reader().forType(new TypeReference<List<Company>>(){});
            companies = objectReader.readValue(h.get("result").get("companies").get("company"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Company c : companies){
            //fetch v https://api.foursquare.com/v2/venues/search?ll={c.address[0].coordinate.north,c.address[0].coordinate.east}&client_id=CLIENT_ID&client_secret=CLIENT_SECRET&intent=match&name={c.displayName}&v=20180401

            LOGGER.log(Level.INFO, "COMPANY == " + c.toString());

            if(c.getAddress() == null){
                LOGGER.log(Level.INFO, "No Address");
                continue;
            }

            if(c.getAddress().get(0).getCoordinate() == null){
                LOGGER.log(Level.INFO, "No Coordinates");
                continue;
            }

            JsonNode v = getData("https://api.foursquare.com/v2/venues/search?ll="
                    + c.getAddress().get(0).getCoordinate().getNorth() + ","
                    + c.getAddress().get(0).getCoordinate().getEast()
                    + "&client_id=" + CLIENT_ID
                    + "&client_secret=" + CLIENT_SECRET
                    + "&intent=match&name="
                    + encodeValue(c.getDisplayName()) + "&v=20180401");


            //fetch d https://api.foursquare.com/v2/venues/${v.response.venues[0].id}/photos?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&v=20180401
            objectReader =  new ObjectMapper().reader().forType(new TypeReference<List<Venues>>(){});
            List<Venues> venues = null;

            try {
                venues = objectReader.readValue(v.get("response").get("venues"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(venues == null || venues.isEmpty()){
                LOGGER.log(Level.INFO, "No Venues");
                continue;
            }

            JsonNode d = getData("https://api.foursquare.com/v2/venues/"
                    + venues.get(0).getId()
                    + "/photos?client_id=" + CLIENT_ID
                    + "&client_secret=" + CLIENT_SECRET
                    + "&v=20180401");


            //foreach i d.response.photo.items
            objectReader = new ObjectMapper().reader().forType(new TypeReference<List<Items>>(){});
            List<Items> items = null;
            try {
                items = objectReader.readValue(d.get("response").get("photos").get("items"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(items == null || items.isEmpty()){
                LOGGER.log(Level.INFO, "No Items");
                continue;
            }




            for (Items i : items){
                //download {i.prefix}original{i.suffix} /some_local_path/photos/{c.id}_{i.suffix}
                LOGGER.log(Level.INFO,"FILE NAME== " + i.getSuffix().replace("\\", ""));
                downloadFromURL(i.getPrefix() + "original" + i.getSuffix(),
                        new File( downloadPath + i.getSuffix().replaceFirst("/", "")));
            }

            exchange.response(new JsonResponse().with("Downloads are at location", downloadPath));
        }

    }


    private JsonNode getData(String url){
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


    private void downloadFromURL(String urlString, File destination) {
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


    public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
