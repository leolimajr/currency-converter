package com.leolimajr.sample.service;

import java.util.Objects;

import com.leolimajr.sample.domain.Customer;
import com.leolimajr.sample.repository.CustomerRepository;
import com.leolimajr.sample.repository.specification.CustomerSpecifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

	protected CustomerRepository repository;

	@Autowired
	public SecurityService(CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Objects.requireNonNull(username);

		Customer customer = this.repository
				.findOne(CustomerSpecifications.findByUsername(username));
		if (customer == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(customer.getUsername(), customer.getPassword(),
				AuthorityUtils.createAuthorityList(customer.getAuthority()));
	}
}