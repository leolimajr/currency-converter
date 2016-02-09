package com.leolimajr.sample.service;

import java.util.Objects;

import javax.transaction.Transactional;

import com.leolimajr.sample.domain.Customer;
import com.leolimajr.sample.domain.converter.CustomerConverter;
import com.leolimajr.sample.form.CustomerForm;
import com.leolimajr.sample.repository.CustomerRepository;
import com.leolimajr.sample.service.exception.UsernameAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

	protected CustomerConverter converter;
	protected CustomerRepository repository;

	@Autowired
	public SignupService(CustomerConverter converter, CustomerRepository repository) {
		this.converter = converter;
		this.repository = repository;
	}

	@Transactional
	public Customer save(CustomerForm form) throws UsernameAlreadyExistsException {

		Objects.requireNonNull(form);
		Objects.requireNonNull(form.getUsername());
		Objects.requireNonNull(form.getStreet());
		Objects.requireNonNull(form.getZipCode());
		Objects.requireNonNull(form.getCity());
		Objects.requireNonNull(form.getCountry());
		Objects.requireNonNull(form.getPassword());

		Customer customer = this.converter.from(form);
		if (this.repository.exists(customer.getUsername())) {
			throw new UsernameAlreadyExistsException(
					String.format("Username %s already exists!", customer.getUsername()));
		}
		customer.setAuthority("ROLE_USER");
		return this.repository.save(customer);
	}
}