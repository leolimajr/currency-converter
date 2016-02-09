package com.leolimajr.sample.domain.enums;

public enum Currency {
	
	AUD("Australian Dollar"),
	BRL("Brazilian Real"),
	GBP("British Pound Sterling"),
	CAD("Canadian Dollar"),
	EUR("Euro"),
	JPY("Japanese Yen"),
	NZD("New Zealand Dollar"),
	CHF("Swiss Franc"),
	USD("United States Dollar");

	private final String name;

	Currency(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
