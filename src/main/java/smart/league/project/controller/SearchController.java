package smart.league.project.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import smart.league.project.controller.handler.FetchHandler;
import smart.league.project.server.exchange.HttpExchange;
import smart.league.project.server.exchange.RequestHandler;
import smart.league.project.server.exchange.Response;
import smart.league.project.util.async.Computation;
import smart.league.project.util.async.ExecutorsProvider;
import smart.league.project.util.web.json.JsonResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/")
public class SearchController {

	@Inject
	private FetchHandler fetchHandler;
	@Inject
	private ExecutorsProvider executorsProvider;

	@GetMapping("search")
	@Async
	public CompletableFuture<ResponseEntity<Object>> search() {

		HttpExchange exchange = new HttpExchange();

		RequestHandler requestHandler = RequestHandler.route("/search")
				.handler(fetchHandler);

		ExecutorService executorService = executorsProvider.getExecutorService();
	    CompletableFuture<ResponseEntity<Object>> asyncResponse = Computation.computeAsync(() -> handleRequest(requestHandler, exchange), executorService)
		        .thenApplyAsync(response -> ok(response), executorService)
		        .exceptionally(error -> handleException(error));
		
	    return asyncResponse;

	}

	@PostMapping(path = "/download")
	@Async
	public CompletableFuture<ResponseEntity<Object>> download(@RequestBody JsonNode node) {

		HttpExchange exchange = new HttpExchange();


		RequestHandler requestHandler = RequestHandler.route("/download")
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
