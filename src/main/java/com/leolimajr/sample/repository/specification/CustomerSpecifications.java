package com.leolimajr.sample.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.leolimajr.sample.domain.Customer;

import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

	public static Specification<Customer> findByUsername(final String email) {
		return new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				return cb.equal(root.get("username"), email);
			}
		};
	}
}