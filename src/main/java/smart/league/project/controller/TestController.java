package smart.league.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smart.league.project.controller.handler.InputValidationHandler;
import smart.league.project.controller.handler.PalindromeHandler;
import smart.league.project.server.exchange.HttpExchange;
import smart.league.project.server.exchange.RequestHandler;
import smart.league.project.server.exchange.Response;
import smart.league.project.util.async.Computation;
import smart.league.project.util.async.ExecutorsProvider;
import smart.league.project.util.web.json.JsonResponse;

import javax.inject.Inject;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/")
public class TestController {

	@Inject
	private PalindromeHandler palindromeHandler;

	@Inject
	private InputValidationHandler inputValidationHandler;

	@Inject
	private ExecutorsProvider executorsProvider;

	@GetMapping("test")
	@Async
	public Callable<ResponseEntity<Object>> test(@RequestParam("param") String param) {
		
		HttpExchange exchange = new HttpExchange()
				.addQueryParam("param", param);

		RequestHandler requestHandler = RequestHandler.route("/test")
				.handler(inputValidationHandler)
				.handler(palindromeHandler);

		ExecutorService executorService = executorsProvider.getExecutorService();
	    CompletableFuture<Callable<ResponseEntity<Object>>> asyncResponse = Computation.computeAsync(() -> handleRequest(requestHandler, exchange), executorService)
		        .thenApplyAsync(response -> ok(response), executorService)
		        .exceptionally(error -> handleException(error));
		
		return asyncResponse.join();

	}

	private <T> Response<T> handleRequest(RequestHandler router, HttpExchange exchange) {
		router.handle(exchange);
		return exchange.getResponse();

	}

	private <T> Callable<ResponseEntity<T>> ok(Response<T> response) {
		return () -> ResponseEntity.ok(response.getBody());
	}

	@SuppressWarnings("unchecked")
	private <T> Callable<ResponseEntity<T>> handleException(Throwable ex) {
		T json = (T) new JsonResponse()
		                          .with("Status", "INTERNAL_SERVER_ERROR")
		                          .with("Code", 500)
		                          .with("Exception", ex)
		                          .done();
		return () -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(json);
	}
}
