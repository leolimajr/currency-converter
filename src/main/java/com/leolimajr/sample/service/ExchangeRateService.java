package com.leolimajr.sample.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Leo Lima
 */
@Service
@CacheConfig(cacheNames = "rates")
public class ExchangeRateService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExchangeRateService.class);

	public static final int DEFAULT_SCALE = 6;

	public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(DEFAULT_SCALE,
			RoundingMode.HALF_EVEN);

	protected RestTemplate restTemplate;
	protected String latestRatesUrl;
	protected String historicalRatesUrl;
	protected CounterService counterService;

	@Autowired
	public ExchangeRateService(
			@Value("${org.openexchangerates.latest.url}") String latestRatesUrl,
			@Value("${org.openexchangerates.historical.url}") String historicalRatesUrl,
			CounterService counterService) {

		this.counterService = counterService;
		this.restTemplate = new RestTemplate();
		this.latestRatesUrl = latestRatesUrl;
		this.historicalRatesUrl = historicalRatesUrl;
	}

	protected HttpEntity<String> setupHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
		headers.set(HttpHeaders.ACCEPT, "application/json");
		headers.set(HttpHeaders.ACCEPT_ENCODING, "GZIP");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return entity;
	}

	protected Map<Currency, BigDecimal> getRates(String url)
			throws CannotGetExchangeRateException {
		ResponseEntity<String> result = this.restTemplate.exchange(url, HttpMethod.GET,
				setupHeaders(), String.class);
		ObjectMapper mapper = new ObjectMapper();
		if (result.getStatusCode() == HttpStatus.OK && result.getBody() != null) {
			JsonNode node;
			try {
				node = mapper.readTree(result.getBody());
				return this.convertData(node);
			}
			catch (JsonProcessingException e) {
				this.counterService
						.increment("counter.exchange.integration.response.json.error");
				LOGGER.error(
						"JSON format not supported! (maybe a document structure change?)",
						e);
			}
			catch (IOException e) {
				this.counterService
						.increment("counter.exchange.integration.network.error");
				LOGGER.error(
						String.format("IOException trying to get rates, URL: %s", url),
						e);
			}
		}
		else {
			this.counterService
					.increment("counter.exchange.integration.http.status.error");
			LOGGER.error(String.format(
					"Invalid Response! Exchange Provider Error? Status Code Returned: %s, Response Body: %s",
					result.getStatusCode(), result.getBody()));
		}
		throw new CannotGetExchangeRateException(
				"Could not get Exchange Rates. Please see logs for details.");
	}

	protected Map<Currency, BigDecimal> convertData(JsonNode node)
			throws CannotGetExchangeRateException {
		Map<Currency, BigDecimal> data = new HashMap<Currency, BigDecimal>(
				Currency.values().length);
		if (node.has("rates")) {
			JsonNode rates = node.get("rates");
			for (Currency currency : Currency.values()) {
				if (rates.has(currency.toString())) {
					data.put(currency,
							new BigDecimal(rates.get(currency.toString()).asDouble(),
									DEFAULT_MATH_CONTEXT));
				}
			}
		}
		if (data.isEmpty()) {
			throw new CannotGetExchangeRateException(String.format(
					"JSON Node is empty or invalid format (contract change?). Node: %s",
					node));
		}
		return data;
	}

	protected void validateArgs(Integer year, Integer month, Integer day) {
		try {
			LocalDate past = LocalDate.of(year, month, day);
			LocalDate now = LocalDate.now();
			if (now.toEpochDay() <= past.toEpochDay()) {
				throw new IllegalArgumentException(
						"Date must be at least a day before today");
			}
			if (year < 1999) {
				throw new IllegalArgumentException(
						"Year must be equals or greater than 1999.");
			}
		}
		catch (DateTimeException e) {
			throw new IllegalArgumentException("Invalid Date!");
		}
	}

	@Cacheable("historicalRates")
	public Map<Currency, BigDecimal> getHistoricalRates(Integer year, Integer month,
			Integer day) throws CannotGetExchangeRateException {
		validateArgs(year, month, day);
		this.counterService.increment("counter.exchange.integration.historical.success");
		return this.getRates(String.format(this.historicalRatesUrl, year, month, day));
	}

	@Cacheable("rates")
	public Map<Currency, BigDecimal> getRates() throws CannotGetExchangeRateException {
		this.counterService.increment("counter.exchange.integration.latest.success");
		return this.getRates(this.latestRatesUrl);
	}
}