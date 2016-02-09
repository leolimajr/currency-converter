package com.leolimajr.sample.form;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.leolimajr.sample.domain.enums.Country;

import org.springframework.format.annotation.DateTimeFormat;

public class CustomerForm {

	@NotNull
	private String username;
	@NotNull
	@Size(min = 8, max = 64)
	private String password;
	@NotNull
	@Size(min = 8, max = 64)
	private String confirmPassword;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	@Past
	private Date birthDate;
	@NotNull
	@Size(min = 4, max = 100)
	private String street;
	@NotNull
	@Size(min = 3, max = 10)
	private String zipCode;
	@NotNull
	@Size(min = 3, max = 64)
	private String city;
	@NotNull
	private Country country;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
}