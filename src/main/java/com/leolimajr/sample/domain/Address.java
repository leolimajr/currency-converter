package com.leolimajr.sample.domain;

import javax.persistence.Embeddable;

import com.leolimajr.sample.domain.enums.Country;

@Embeddable
public class Address {

	private String street;
	private String zipCode;
	private String city;
	private Country country;

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}