package smart.league.project.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import smart.league.project.controller.handler.FetchHandler;
import smart.league.project.controller.handler.InputValidationHandler;
import smart.league.project.controller.handler.PalindromeHandler;
import smart.league.project.service.FetchService;
import smart.league.project.service.PalindromeService;

@Configuration
public class BeanProducer {


	@Bean
	public PalindromeService producePalindromService() {
		return new PalindromeService();
	}

	@Bean
	public PalindromeHandler producePalindromHandler(PalindromeService palindromeService) {
		return new PalindromeHandler(palindromeService);
	}

	@Bean
	public InputValidationHandler produceInputValidationHandler() {
		return new InputValidationHandler();
	}

	@Bean
	public FetchService fetchService() {
		return new FetchService();
	}

	@Bean
	public FetchHandler producefetchHandler(FetchService fetchService) {
		return new FetchHandler(fetchService);
	}
}
