package com.leolimajr.sample.domain;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	private String username;
	private String password;
	private String authority;
	private Date birthDate;
	@Embedded
	private Address address;

	public Customer() {
	}

	public Customer(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String email) {
		this.username = email;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}