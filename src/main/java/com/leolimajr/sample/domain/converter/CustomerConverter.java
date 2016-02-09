package com.leolimajr.sample.domain.converter;

import com.leolimajr.sample.domain.Customer;
import com.leolimajr.sample.form.CustomerForm;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.stereotype.Component;

/**
 * @author Leo Lima
 */

@Component
public class CustomerConverter {

	private final ModelMapper modelMapper;

	public CustomerConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
	}

	public Customer from(CustomerForm form) {
		return this.modelMapper.map(form, Customer.class);
	}

	public CustomerForm to(Customer customer) {
		return this.modelMapper.map(customer, CustomerForm.class);
	}
}