package com.leolimajr.sample.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.leolimajr.sample.domain.CurrencyConversion;
import com.leolimajr.sample.domain.Customer;

import org.springframework.data.jpa.domain.Specification;

public class CurrencyConversionSpecifications {

	public static Specification<CurrencyConversion> findByCustomer(
			final Customer customer) {
		return new Specification<CurrencyConversion>() {
			@Override
			public Predicate toPredicate(Root<CurrencyConversion> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				return cb.equal(root.get("customer"), customer);
			}
		};
	}
}