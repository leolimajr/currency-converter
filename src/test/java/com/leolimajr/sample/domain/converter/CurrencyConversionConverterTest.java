package com.leolimajr.sample.domain.converter;

import java.math.BigDecimal;
import java.util.Date;

import com.leolimajr.sample.domain.CurrencyConversion;
import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.form.CurrencyConverterForm;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Leo Lima
 */

public class CurrencyConversionConverterTest {

	@Test
	public void shouldConvertFormToDomain() {
		CurrencyConversionConverter converter = new CurrencyConversionConverter();
		CurrencyConverterForm form = new CurrencyConverterForm();
		form.setFrom(Currency.BRL);
		form.setTo(Currency.AUD);
		form.setAmount(BigDecimal.valueOf(1));
		form.setReference(new Date());
		form.setRate(BigDecimal.valueOf(1));
		CurrencyConversion conversion = converter.from(form);
		assertThat(form.getFrom(), equalTo(conversion.getFrom()));
		assertThat(form.getTo(), equalTo(conversion.getTo()));
		assertThat(form.getAmount(), equalTo(conversion.getAmount()));
		assertThat(form.getFrom(), equalTo(conversion.getFrom()));
		assertThat(form.getReference(), equalTo(conversion.getReference()));
		assertThat(form.getRate(), equalTo(conversion.getRate()));
		assertThat(conversion.getId(), is(nullValue()));
	}

	@Test
	public void shouldConvertFromDomainToForm() {
		CurrencyConversionConverter converter = new CurrencyConversionConverter();
		CurrencyConversion conversion = new CurrencyConversion();
		conversion.setFrom(Currency.BRL);
		conversion.setTo(Currency.AUD);
		conversion.setAmount(BigDecimal.valueOf(1d));
		conversion.setReference(new Date());
		conversion.setRate(BigDecimal.valueOf(1d));
		CurrencyConverterForm form = converter.to(conversion);
		assertThat(form.getFrom(), equalTo(conversion.getFrom()));
		assertThat(form.getTo(), equalTo(conversion.getTo()));
		assertThat(form.getAmount(), equalTo(conversion.getAmount()));
		assertThat(form.getFrom(), equalTo(conversion.getFrom()));
		assertThat(form.getReference(), equalTo(conversion.getReference()));
		assertThat(form.getRate(), equalTo(conversion.getRate()));
		assertThat(conversion.getId(), is(nullValue()));
	}
}