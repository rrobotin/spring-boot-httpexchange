package smart.league.project.controller.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import smart.league.project.objects.Company;
import smart.league.project.objects.Items;
import smart.league.project.objects.Venues;
import smart.league.project.server.exchange.HttpExchange;
import smart.league.project.server.handler.Handler;
import smart.league.project.service.FetchService;
import smart.league.project.util.web.json.JsonResponse;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchHandler implements Handler<HttpExchange> {

    private static final Logger LOGGER = Logger.getLogger(FetchHandler.class.getName());
    private static final String CLIENT_ID = "JTKH3M3SPDDB50EZOMPPZVZT5E3LBFX5IYCK0BGGVDJJP2S3";
    private static final String CLIENT_SECRET = "0VUR2LEHRVMXZL52IO02NP14LUG1MIVIJVL5IIWPLUBMFYDI";

    private FetchService fetchService;
    private ObjectMapper mapper =
            new ObjectMapper();

    public FetchHandler() {
    }

    @Inject
    public FetchHandler(FetchService fetchService) {
        this.fetchService = fetchService;
    }


    @Override
    public void handle(HttpExchange exchange) {





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
        JsonNode h = fetchService.getData("https://api.hitta.se/search/v7/app/combined/within/57.840703831916%3A11.728156448084002%2C57.66073920808401%3A11.908121071915998/?range.to=101&range.from=1&geo.hint=57.75072152%3A11.81813876&sort.order=relevance&query=lunch");


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

            JsonNode v = fetchService.getData("https://api.foursquare.com/v2/venues/search?ll="
                        + c.getAddress().get(0).getCoordinate().getNorth() + ","
                        + c.getAddress().get(0).getCoordinate().getEast()
                        + "&client_id=" + CLIENT_ID
                        + "&client_secret=" + CLIENT_SECRET
                        + "&intent=match&name="
                        + fetchService.encodeValue(c.getDisplayName()) + "&v=20180401");

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

            JsonNode d = fetchService.getData("https://api.foursquare.com/v2/venues/"
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
                fetchService.downloadFromURL(i.getPrefix() + "original" + i.getSuffix(),
                        new File( downloadPath + i.getSuffix().replaceFirst("/", "")));
            }

            exchange.response(new JsonResponse().with("Downloads are at location", downloadPath));
        }

    }

}
