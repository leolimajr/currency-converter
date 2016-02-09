package com.leolimajr.sample.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.leolimajr.sample.domain.enums.Currency;

@Entity
public class CurrencyConversion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "from_currency")
	private Currency from;
	private Currency to;
	private BigDecimal amount;
	private BigDecimal rate;
	private Date reference;

	@ManyToOne
	@JoinColumn
	private Customer customer;

	public CurrencyConversion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Currency getFrom() {
		return this.from;
	}

	public void setFrom(Currency from) {
		this.from = from;
	}

	public Currency getTo() {
		return this.to;
	}

	public void setTo(Currency to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getReference() {
		return this.reference;
	}

	public void setReference(Date reference) {
		this.reference = reference;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}