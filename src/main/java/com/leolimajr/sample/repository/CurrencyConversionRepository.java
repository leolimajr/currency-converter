package com.leolimajr.sample.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.leolimajr.sample.domain.CurrencyConversion;

public interface CurrencyConversionRepository
		extends CrudRepository<CurrencyConversion, Integer>,
		JpaSpecificationExecutor<CurrencyConversion> {
}