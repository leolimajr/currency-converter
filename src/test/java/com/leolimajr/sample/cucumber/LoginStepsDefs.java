package com.leolimajr.sample.cucumber;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.leolimajr.sample.BaseAcceptanceTestConfig;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class LoginStepsDefs extends BaseAcceptanceTestConfig {

	protected HtmlPage loginPage;
	protected HtmlForm loginForm;

	@Before
	public void setup() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("schema.sql").addScript("dml-login.sql").build();
	}

	@Given("^I am on the login page$")
	public void iAmOnTheLoginPage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.loginPage = this.webClient
				.getPage("http://localhost:" + this.port + "/login");
		this.loginForm = this.loginPage.getFormByName("form");
		assertNotNull(this.loginPage.getFirstByXPath("//form[@action='/login']"));
	}

	@When("^I provide the username \"([^\"]*)\"$")
	public void iProvideTheEmailAddress(String username)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.loginForm.getInputByName("username").setValueAttribute(username);
	}

	@And("^I provide the password \"([^\"]*)\"$")
	public void iProvideThePassword(String password)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.loginForm.getInputByName("password").setValueAttribute(password);

	}

	@Then("^I should be successfully logged in$")
	public void iShouldBeSuccessfullyLoggedIn()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlSubmitInput submit = this.loginPage
				.getFirstByXPath("//input[@type='submit']");
		assertThat(submit.click().getUrl().getPath(), equalTo("/currency/converter"));
	}
}
