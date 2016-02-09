package com.leolimajr.sample.form;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.leolimajr.sample.domain.enums.Currency;

import org.springframework.format.annotation.DateTimeFormat;

public class CurrencyConverterForm {

	@NotNull
	private Currency from;
	@NotNull
	private Currency to;
	@NotNull
	private BigDecimal amount;
	private BigDecimal rate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past
	private Date reference;

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

	public String getFormattedAmount() {
		return getFormatted(getTo(), this.getAmount());
	}

	public String getFormattedRate() {
		return getFormatted(getFrom(), this.getRate());
	}

	protected String getFormatted(Currency currency, BigDecimal value) {
		Objects.requireNonNull(currency);
		Objects.requireNonNull(value);
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		formatter.setCurrency(java.util.Currency.getInstance(currency.toString()));
		return formatter.format(value);
	}
}
