package com.leolimajr.sample.controller;

import java.util.Arrays;

import com.leolimajr.sample.BaseWebIntegrationTest;
import org.junit.Test;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.TestRestTemplate.HttpClientOption;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginControllerIT extends BaseWebIntegrationTest {

	@Test
	public void shouldAllowLoginPage() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/login", HttpMethod.GET,
				new HttpEntity<Void>(headers), String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue("Wrong content:\n" + entity.getBody(),
				entity.getBody().contains("form-signin"));
	}

	@Test
	public void shouldAllowLoginWithValidCredential() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "user@test.com");
		form.set("password", "userpassword");
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/login", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		assertEquals(HttpStatus.FOUND, entity.getStatusCode());
		assertTrue("Wrong location:\n" + entity.getHeaders(),
				entity.getHeaders().getLocation().toString().endsWith(this.port + "/"));
		assertNotNull("Missing cookie:\n" + entity.getHeaders(),
				entity.getHeaders().get("Set-Cookie"));
	}

	@Test
	public void shouldNotAllowInvalidUsername() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "foo");
		form.set("password", "testuser");
		ResponseEntity<String> entity = new TestRestTemplate(
				HttpClientOption.ENABLE_REDIRECTS).exchange(
						"http://localhost:" + this.port + "/login", HttpMethod.POST,
						new HttpEntity<MultiValueMap<String, String>>(form, headers),
						String.class);
		assertEquals(HttpStatus.FOUND, entity.getStatusCode());
		assertTrue(entity.getHeaders().getLocation().getPath().equals("/login"));
		assertTrue(entity.getHeaders().getLocation().getQuery().contains("error"));
	}

	@Test
	public void shouldNotAllowInvalidPassword() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "testuser");
		form.set("password", "bar");
		ResponseEntity<String> entity = new TestRestTemplate(
				HttpClientOption.ENABLE_REDIRECTS).exchange(
						"http://localhost:" + this.port + "/login", HttpMethod.POST,
						new HttpEntity<MultiValueMap<String, String>>(form, headers),
						String.class);
		assertEquals(HttpStatus.FOUND, entity.getStatusCode());
		assertTrue(entity.getHeaders().getLocation().getPath().equals("/login"));
		assertTrue(entity.getHeaders().getLocation().getQuery().contains("error"));
	}
}