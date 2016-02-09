
package com.leolimajr.sample.service;

import java.math.BigDecimal;

import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Leo Lima
 */
public class CurrencyConverterServiceTest {

	@Test
	public void shouldCalculateWithCorrectPrecision()
			throws CannotGetExchangeRateException {
		ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
		CurrencyConverterService service = new CurrencyConverterService(
				exchangeRateService);
		BigDecimal result = service.calculateRate(BigDecimal.valueOf(4.05d),
				BigDecimal.valueOf(1.43d), BigDecimal.valueOf(1d));
		assertThat(5, is(result.scale()));
		assertThat(2.83217, is(result.doubleValue()));
	}
}