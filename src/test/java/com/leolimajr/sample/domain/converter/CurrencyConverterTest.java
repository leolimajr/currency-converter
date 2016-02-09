package com.leolimajr.sample.domain.converter;

import java.util.Date;

import com.leolimajr.sample.domain.Address;
import com.leolimajr.sample.domain.Customer;
import com.leolimajr.sample.domain.enums.Country;
import com.leolimajr.sample.form.CustomerForm;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Leo Lima
 */

public class CurrencyConverterTest {

	@Test
	public void shouldConvertFormToDomain() {
		CustomerConverter converter = new CustomerConverter();
		CustomerForm customerForm = new CustomerForm();
		customerForm.setUsername("leo@leo.com");
		customerForm.setStreet("Foo St, 0");
		customerForm.setBirthDate(new Date());
		customerForm.setCity("Mainstrabe");
		customerForm.setPassword("password");
		customerForm.setConfirmPassword("password2");
		customerForm.setZipCode("85579");
		customerForm.setCountry(Country.DE);
		Customer customer = converter.from(customerForm);
		assertThat(customerForm.getUsername(), equalTo(customer.getUsername()));
		assertThat(customerForm.getStreet(), equalTo(customer.getAddress().getStreet()));
		assertThat(customerForm.getBirthDate(), equalTo(customer.getBirthDate()));
		assertThat(customerForm.getCity(), equalTo(customer.getAddress().getCity()));
		assertThat(customerForm.getPassword(), equalTo(customer.getPassword()));
		assertThat(customerForm.getZipCode(),
				equalTo(customer.getAddress().getZipCode()));
		assertThat(customerForm.getCountry(),
				equalTo(customer.getAddress().getCountry()));
		assertThat(customer.getAuthority(), is(nullValue()));
	}

	@Test
	public void shouldConvertFromDomainToForm() {
		CustomerConverter converter = new CustomerConverter();
		Customer customer = new Customer();
		customer.setAddress(new Address());
		customer.setUsername("leo@leo.com");
		customer.getAddress().setStreet("Foo St, 0");
		customer.setBirthDate(new Date());
		customer.getAddress().setCity("Mainstrabe");
		customer.setPassword("password");
		customer.getAddress().setZipCode("85579");
		customer.getAddress().setCountry(Country.DE);
		CustomerForm customerForm = converter.to(customer);
		assertThat(customerForm.getUsername(), equalTo(customer.getUsername()));
		assertThat(customerForm.getStreet(), equalTo(customer.getAddress().getStreet()));
		assertThat(customerForm.getBirthDate(), equalTo(customer.getBirthDate()));
		assertThat(customerForm.getCity(), equalTo(customer.getAddress().getCity()));
		assertThat(customerForm.getPassword(), equalTo(customer.getPassword()));
		assertThat(customerForm.getZipCode(),
				equalTo(customer.getAddress().getZipCode()));
		assertThat(customerForm.getCountry(),
				equalTo(customer.getAddress().getCountry()));
	}
}