package smart.league.project.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import smart.league.project.controller.handler.FetchHandler;
import smart.league.project.server.exchange.HttpExchange;
import smart.league.project.server.exchange.RequestHandler;
import smart.league.project.server.exchange.Response;
import smart.league.project.service.FetchService;
import smart.league.project.util.async.Computation;
import smart.league.project.util.async.ExecutorsProvider;
import smart.league.project.util.map.MultiValueMap;
import smart.league.project.util.web.json.JsonResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/")
public class SearchController {

	@Inject
	private FetchHandler fetchHandler;
	@Inject
	private ExecutorsProvider executorsProvider;

	@GetMapping("getPics")
	@Async
	public CompletableFuture<ResponseEntity<Object>> getPics() {

		HttpExchange exchange = new HttpExchange();
		exchange.setMethod(HttpMethod.GET);

		RequestHandler requestHandler = RequestHandler.route("/getPics")
				.handler(fetchHandler);

		ExecutorService executorService = executorsProvider.getExecutorService();
	    CompletableFuture<ResponseEntity<Object>> asyncResponse = Computation.computeAsync(() -> handleRequest(requestHandler, exchange), executorService)
		        .thenApplyAsync(response -> ok(response), executorService)
		        .exceptionally(error -> handleException(error));
		
	    return asyncResponse;

	}

	@PostMapping("search")
	@Async
	public CompletableFuture<ResponseEntity<Object>> search(@RequestBody JsonNode payload, @RequestParam String type) {

		HttpExchange exchange = new HttpExchange();
		exchange.addQueryParam("type", type);
		exchange.setMethod(HttpMethod.POST);
		exchange.setRoute("search");

		Map<String,Object> body = new HashMap<>();

		ObjectReader objectReader =  new ObjectMapper().reader().forType(new TypeReference<Map<String,Object>>(){});
		try {
			body = objectReader.readValue(payload);
		} catch (IOException e) {
			e.printStackTrace();
		}

		exchange.setBody(body);

		RequestHandler requestHandler = RequestHandler.route("/search")
				.handler(fetchHandler);

		ExecutorService executorService = executorsProvider.getExecutorService();
		CompletableFuture<ResponseEntity<Object>> asyncResponse = Computation.computeAsync(() -> handleRequest(requestHandler, exchange), executorService)
				.thenApplyAsync(response -> ok(response), executorService)
				.exceptionally(error -> handleException(error));

		return asyncResponse;

	}

	private <T> Response<T> handleRequest(RequestHandler requestHandler, HttpExchange exchange) {
		requestHandler.handle(exchange);
		return exchange.getResponse();
	}

	private <T> ResponseEntity<T> ok(Response<T> response) {
		return ResponseEntity.ok(response.getBody());
	}

	@SuppressWarnings("unchecked")
	private <T> ResponseEntity<T> handleException(Throwable ex) {
		T json = (T) new JsonResponse()
		                          .with("Status", "INTERNAL_SERVER_ERROR")
		                          .with("Code", 500)
		                          .with("Exception", ex)
		                          .done();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(json);
	}
}
