package com.leolimajr.sample.form.validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.form.CurrencyConverterForm;
import org.junit.Test;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Leo Lima
 */

public class CurrencyConverterFormValidatorTest {

	@Test
	public void shouldSupportModel() {
		CurrencyConverterFormValidator validator = new CurrencyConverterFormValidator();
		assertThat(validator.supports(CurrencyConverterForm.class), is(true));
	}

	@Test
	public void shouldValidateConversion() {
		CurrencyConverterFormValidator validator = new CurrencyConverterFormValidator();
		CurrencyConverterForm form = new CurrencyConverterForm();
		form.setFrom(Currency.AUD);
		form.setTo(Currency.BRL);
		form.setReference(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant()));
		form.setAmount(BigDecimal.valueOf(1d));
		Errors errors = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, errors);
		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFailWithInvalidReference() {
		CurrencyConverterFormValidator validator = new CurrencyConverterFormValidator();
		CurrencyConverterForm form = new CurrencyConverterForm();
		form.setFrom(Currency.AUD);
		form.setTo(Currency.BRL);
		form.setReference(Date.from(LocalDate.of(1990, 1, 1).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant()));
		Errors errors = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getFieldError("reference"), is(notNullValue()));
		form.setReference(new Date());
		errors = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getFieldError("reference"), is(notNullValue()));
	}

	@Test
	public void shouldFailWithInvalidAmount() {
		CurrencyConverterFormValidator validator = new CurrencyConverterFormValidator();
		CurrencyConverterForm form = new CurrencyConverterForm();
		form.setFrom(Currency.AUD);
		form.setTo(Currency.BRL);
		form.setAmount(BigDecimal.valueOf(0));
		Errors errors = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getFieldError("amount"), is(notNullValue()));
	}
}