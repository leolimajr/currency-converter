package com.leolimajr.sample.controller;

import java.util.Arrays;

import com.leolimajr.sample.BaseWebIntegrationTest;
import org.junit.Test;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SignupControllerIT extends BaseWebIntegrationTest {

	@Test
	public void testSignupPage() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/signup", HttpMethod.GET,
				new HttpEntity<Void>(headers), String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue("Wrong content:\n" + entity.getBody(),
				entity.getBody().contains("form-signin"));
	}

	@Test
	public void shouldCreateNewCustomer() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "newuser@email.com");
		form.set("birthDate", "01/01/1990");
		form.set("street", "Mainstrabe 10");
		form.set("zipCode", "85579");
		form.set("city", "Munich");
		form.set("country", "DE");
		form.set("password", "newuserpassword");
		form.set("confirmPassword", "newuserpassword");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/signup", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.FOUND, entity.getStatusCode());
		assertTrue(entity.getHeaders().getLocation().getPath().equals("/"));
		assertNull(entity.getHeaders().getLocation().getQuery());
	}

	@Test
	public void shouldNotAllowDuplicatedUsername() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "newuser@email.com");
		form.set("birthDate", "01/01/1990");
		form.set("street", "Mainstrabe 10");
		form.set("zipCode", "85579");
		form.set("city", "Munich");
		form.set("country", "DE");
		form.set("password", "newuserpassword");
		form.set("confirmPassword", "newuserpassword");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/signup", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.FOUND, entity.getStatusCode());
		assertThat(entity.getHeaders().get("Location"),
				containsInAnyOrder("http://localhost:" + this.port + "/"));
		entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/signup", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue(entity.getBody().contains("Username is already taken"));
	}

	@Test
	public void shouldNotAllowInvalidInput() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "newuser@email.com");
		form.set("birthDate", "01/01/2050");
		form.set("street", "Mainstrabe 10");
		form.set("zipCode", "85579");
		form.set("city", "");
		form.set("country", "BR");
		form.set("password", "newuserpassword");
		form.set("confirmPassword", "newuserpassword2");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/signup", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue(entity.getBody().contains("Must be in the past"));
		assertTrue(entity.getBody().contains("Must be at least 16 years old"));
		assertTrue(entity.getBody().contains("Size must be between 3 and 64"));
		assertTrue(entity.getBody().contains("Your passwords don&#39;t match."));
		assertTrue(entity.getBody().contains("Invalid country"));
	}
}