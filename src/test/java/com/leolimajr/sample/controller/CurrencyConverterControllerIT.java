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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CurrencyConverterControllerIT extends BaseWebIntegrationTest {

	protected ResponseEntity<String> getLoginResponse(String username, String password)
			throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", username);
		form.set("password", password);
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/login", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		return entity;
	}

	protected HttpHeaders getLoginAuthCookies(String username, String password)
			throws Exception {
		ResponseEntity<String> response = getLoginResponse(username, password);
		HttpHeaders headers = new HttpHeaders();
		String cookie = response.getHeaders().getFirst("Set-Cookie");
		headers.set("Cookie", cookie);
		return headers;
	}

	@Test
	public void shouldNotAllowInvalidInput() throws Exception {
		HttpHeaders headers = getLoginAuthCookies("user@test.com", "userpassword");
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("to", "XXX");
		form.set("amount", "invalid");
		form.set("reference", "10/10/1990");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/currency/converter", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue(entity.getBody().contains("Invalid currency"));
		assertTrue(entity.getBody().contains("Field required"));
		assertTrue(entity.getBody().contains("Invalid format"));
		assertTrue(
				entity.getBody().contains("Must be in the past, but after 31/12/1998"));
	}

	@Test
	public void shouldConvertRate() throws Exception {
		HttpHeaders headers = getLoginAuthCookies("user@test.com", "userpassword");
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("from", "USD");
		form.set("to", "BRL");
		form.set("amount", "1");
		form.set("reference", "10/10/1999");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/currency/converter", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue(entity.getBody().contains("<th scope=\"row\">1</th>"));
		assertTrue(entity.getBody().contains("<td>USD</td>"));
		assertTrue(entity.getBody().contains("<td>BRL</td>"));
		assertTrue(entity.getBody().contains("<td>10/10/1999</td>"));
	}

	@Test
	public void shouldConvertHistoricalRate() throws Exception {
		HttpHeaders headers = getLoginAuthCookies("user@test.com", "userpassword");
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("from", "USD");
		form.set("to", "BRL");
		form.set("amount", "1");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/currency/converter", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);

		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue(entity.getBody().contains("<th scope=\"row\">1</th>"));
		assertTrue(entity.getBody().contains("<td>USD</td>"));
		assertTrue(entity.getBody().contains("<td>BRL</td>"));
	}
}