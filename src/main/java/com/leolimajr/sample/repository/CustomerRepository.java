package com.leolimajr.sample.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.leolimajr.sample.domain.Customer;

public interface CustomerRepository
		extends CrudRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
}