
package com.leolimajr.sample.service;

import java.math.BigDecimal;
import java.util.Map;

import com.leolimajr.sample.BaseIntegrationTestConfig;
import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

/**
 * @author Leo Lima
 */

public class ExchangeRateServiceIT extends BaseIntegrationTestConfig {

	@Autowired
	private ExchangeRateService service;
	@Autowired
	private CacheManager cacheManager;

	@Test
	public void shouldGetHistoricalRates() throws CannotGetExchangeRateException {
		Map<Currency, BigDecimal> rates = this.service.getHistoricalRates(1999, 1, 1);
		assertThat(rates.keySet(), containsInAnyOrder(Currency.values()));
	}

	@Test
	public void shouldGetRates() throws CannotGetExchangeRateException {
		Map<Currency, BigDecimal> rates = this.service.getRates();
		assertThat(rates.keySet(), containsInAnyOrder(Currency.values()));
	}

	@Test
	public void shouldGetCachedRates() throws CannotGetExchangeRateException {
		Cache cache = this.cacheManager.getCache("rates");
		assertThat(cache, is(notNullValue()));
		cache.clear();
		Map<Currency, BigDecimal> ratesFirstInvocation = this.service.getRates();
		Map<Currency, BigDecimal> ratesSecondInvocation = this.service.getRates();
		assertThat(ratesFirstInvocation, is(sameInstance(ratesSecondInvocation)));
		cache.clear();
		Map<Currency, BigDecimal> ratesThirdInvocation = this.service.getRates();
		assertThat(ratesFirstInvocation, not(sameInstance(ratesThirdInvocation)));
	}

	@Test
	public void shouldGetCachedHistoricalRates() throws CannotGetExchangeRateException {
		int year = 1999;
		int month = 1;
		int day = 1;
		Cache cache = this.cacheManager.getCache("historicalRates");
		assertThat(cache, is(notNullValue()));
		cache.clear();
		Map<Currency, BigDecimal> ratesFirstInvocation = this.service
				.getHistoricalRates(year, month, day);
		Map<Currency, BigDecimal> ratesSecondInvocation = this.service
				.getHistoricalRates(year, month, day);
		assertThat(ratesFirstInvocation, is(sameInstance(ratesSecondInvocation)));
		cache.clear();
		Map<Currency, BigDecimal> ratesThirdInvocation = this.service
				.getHistoricalRates(year, month, day);
		assertThat(ratesFirstInvocation, not(sameInstance(ratesThirdInvocation)));
	}
}
