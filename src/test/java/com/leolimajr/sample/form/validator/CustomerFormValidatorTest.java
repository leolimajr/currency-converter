package com.leolimajr.sample.form.validator;

import java.time.LocalDate;
import java.util.Date;

import com.leolimajr.sample.domain.enums.Country;
import com.leolimajr.sample.form.CustomerForm;
import org.junit.Test;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Leo Lima
 */

public class CustomerFormValidatorTest {

	@Test
	public void shouldSupportModel() {
		CustomerFormValidator validator = new CustomerFormValidator();
		assertThat(validator.supports(CustomerForm.class), is(true));
	}

	@Test
	public void shouldValidateConversion() {
		CustomerFormValidator validator = new CustomerFormValidator();
		CustomerForm customerForm = new CustomerForm();
		customerForm.setUsername("leo@leo.com");
		customerForm.setStreet("Foo St, 0");
		customerForm.setBirthDate(new Date(LocalDate.of(1999, 1, 1).toEpochDay()));
		customerForm.setCity("Mainstrabe");
		customerForm.setPassword("password");
		customerForm.setConfirmPassword("password");
		customerForm.setZipCode("85579");
		customerForm.setCountry(Country.DE);
		Errors errors = new BeanPropertyBindingResult(customerForm, "customerForm");
		validator.validate(customerForm, errors);
		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldFailWithInvalidEmail() {
		CustomerFormValidator validator = new CustomerFormValidator();
		CustomerForm customerForm = new CustomerForm();
		customerForm.setUsername("");
		Errors errors = new BeanPropertyBindingResult(customerForm, "customerForm");
		validator.validate(customerForm, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getFieldError("username"), is(notNullValue()));
	}

	@Test
	public void shouldFailWithInvalidBirthDate() {
		CustomerFormValidator validator = new CustomerFormValidator();
		CustomerForm customerForm = new CustomerForm();
		customerForm.setBirthDate(new Date());
		Errors errors = new BeanPropertyBindingResult(customerForm, "customerForm");
		validator.validate(customerForm, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getFieldError("birthDate"), is(notNullValue()));

	}

	@Test
	public void shouldFailWithMistMatchPassword() {
		CustomerFormValidator validator = new CustomerFormValidator();
		CustomerForm customerForm = new CustomerForm();
		customerForm.setPassword("foo");
		customerForm.setPassword("bar");
		Errors errors = new BeanPropertyBindingResult(customerForm, "customerForm");
		validator.validate(customerForm, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getFieldError("confirmPassword"), is(notNullValue()));
	}
}