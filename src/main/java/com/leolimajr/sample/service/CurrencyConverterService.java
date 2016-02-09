package com.leolimajr.sample.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leo Lima
 */
@Service
public class CurrencyConverterService {

	protected ExchangeRateService exchangeConverterService;

	@Autowired
	public CurrencyConverterService(ExchangeRateService exchangeConverterService) {
		this.exchangeConverterService = exchangeConverterService;
	}

	protected BigDecimal calculateRate(BigDecimal from, BigDecimal to,
			BigDecimal amount) {
		// all rates are USD based, so simple division will do the conversion
		return from.divide(to, ExchangeRateService.DEFAULT_MATH_CONTEXT).multiply(amount,
				ExchangeRateService.DEFAULT_MATH_CONTEXT);
	}

	public BigDecimal convert(Currency from, Currency to, BigDecimal amount)
			throws CannotGetExchangeRateException {

		Objects.requireNonNull(from);
		Objects.requireNonNull(from);
		Objects.requireNonNull(amount);

		BigDecimal fromUsdValue = this.exchangeConverterService.getRates().get(from);
		BigDecimal toUsdValue = this.exchangeConverterService.getRates().get(to);
		return calculateRate(fromUsdValue, toUsdValue, amount);
	}

	public BigDecimal convert(Currency from, Currency to, BigDecimal amount,
			Date historicDate) throws CannotGetExchangeRateException {

		Objects.requireNonNull(from);
		Objects.requireNonNull(from);
		Objects.requireNonNull(historicDate);

		LocalDate date = historicDate.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		BigDecimal fromUsdValue = this.exchangeConverterService.getHistoricalRates(
				date.getYear(), date.getMonthValue(), date.getDayOfMonth()).get(from);
		BigDecimal toUsdValue = this.exchangeConverterService.getHistoricalRates(
				date.getYear(), date.getMonthValue(), date.getDayOfMonth()).get(to);
		return calculateRate(fromUsdValue, toUsdValue, amount);
	}
}