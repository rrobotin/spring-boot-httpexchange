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
import smart.league.project.util.map.MultiValueMap;
import smart.league.project.util.web.json.JsonResponse;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchHandler implements Handler<HttpExchange> {



    private FetchService fetchService;

    public FetchHandler() {
    }

    @Inject
    public FetchHandler(FetchService fetchService) {
        this.fetchService = fetchService;
    }


    @Override
    public void handle(HttpExchange exchange) {

        switch (exchange.getMethod()){
            case GET:
                fetchService.getPics(exchange);
            case POST:
                fetchService.downloadSource(exchange.getQueryParameters(), exchange.getBody());
        }





    }




}
