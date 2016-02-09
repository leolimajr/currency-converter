package com.leolimajr.sample.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Leo Lima
 */
public class ExchangeRateServiceTest {

	private ExchangeRateService service;

	private String ratesUrl;
	private String historicalRatesUrl;

	@Before
	public void setup() throws IOException {
		Properties props = new Properties();
		props.load(ExchangeRateServiceTest.class
				.getResourceAsStream("/application.properties"));
		this.ratesUrl = props.getProperty("org.openexchangerates.latest.url");
		this.historicalRatesUrl = props
				.getProperty("org.openexchangerates.historical.url");
		this.service = new ExchangeRateService(this.ratesUrl, this.historicalRatesUrl,
				Mockito.mock(CounterService.class));
		this.service.restTemplate = mock(RestTemplate.class);
	}

	@Test(expected = CannotGetExchangeRateException.class)
	public void shouldThrownExceptionWhenInvalidStatusCode()
			throws CannotGetExchangeRateException {
		ExchangeRateService service = this.service;
		when(service.restTemplate.exchange(this.ratesUrl, HttpMethod.GET,
				this.service.setupHeaders(), String.class))
						.thenReturn(new ResponseEntity<String>(HttpStatus.BAD_REQUEST));
		Map<Currency, BigDecimal> rates = service.getRates();
		assertThat(rates.keySet(), contains(Currency.values()));
	}

	@Test(expected = CannotGetExchangeRateException.class)
	public void shouldThrowExceptionWhenNullResponseBody()
			throws CannotGetExchangeRateException {
		ExchangeRateService service = this.service;
		when(service.restTemplate.exchange(this.ratesUrl, HttpMethod.GET,
				this.service.setupHeaders(), String.class))
						.thenReturn(new ResponseEntity<String>(HttpStatus.OK));
		Map<Currency, BigDecimal> rates = service.getRates();
		assertThat(rates.keySet(), contains(Currency.values()));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = CannotGetExchangeRateException.class)
	public void shouldThrowExceptionWhenBrokenJson()
			throws CannotGetExchangeRateException {
		ExchangeRateService service = this.service;
		ResponseEntity<String> responseEntity = Mockito.mock(ResponseEntity.class);
		when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
		when(responseEntity.getBody()).thenReturn("{brokenjson:}");
		when(service.restTemplate.exchange(this.ratesUrl, HttpMethod.GET,
				this.service.setupHeaders(), String.class)).thenReturn(responseEntity);
		Map<Currency, BigDecimal> rates = service.getRates();
		assertThat(rates.keySet(), contains(Currency.values()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenInvalidYear()
			throws CannotGetExchangeRateException {
		Map<Currency, BigDecimal> rates = this.service.getHistoricalRates(1998, 2, 30);
		assertThat(rates.keySet(), contains(Currency.values()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenInvalidMonth()
			throws CannotGetExchangeRateException {
		Map<Currency, BigDecimal> rates = this.service.getHistoricalRates(1998, 13, 1);
		assertThat(rates.keySet(), contains(Currency.values()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenInvalidDay()
			throws CannotGetExchangeRateException {
		Map<Currency, BigDecimal> rates = this.service.getHistoricalRates(1998, 1, 35);
		assertThat(rates.keySet(), contains(Currency.values()));
	}
}