package com.leolimajr.sample.cucumber;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.leolimajr.sample.BaseAcceptanceTestConfig;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class SignupStepsDefs extends BaseAcceptanceTestConfig {

	protected HtmlPage signupPage;
	protected HtmlForm signupForm;

	@Given("^I am on the sign up page$")
	public void iAmOnTheLoginPage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupPage = this.webClient
				.getPage("http://localhost:" + this.port + "/signup");
		this.signupForm = this.signupPage.getFormByName("form");
		assertNotNull(this.signupPage.getFirstByXPath("//form[@action='/signup']"));
	}

	@When("^I provide the email address \"([^\"]*)\"$")
	public void iProvideTheEmailAddress(String username)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("username").setValueAttribute(username);
	}

	@When("^I provide the birth date \"([^\"]*)\"$")
	public void iProvideTheBirthDate(String birthDate)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("birthDate").setValueAttribute(birthDate);
	}

	@When("^I provide the street address \"([^\"]*)\"$")
	public void iProvideTheStreetAddress(String street)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("street").setValueAttribute(street);
	}

	@And("^I provide the zip code \"([^\"]*)\"$")
	public void iProvideTheZipCode(String zipCode)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("zipCode").setValueAttribute(zipCode);
	}

	@And("^I provide the city name \"([^\"]*)\"$")
	public void iProvideTheCityName(String city)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("city").setValueAttribute(city);
	}

	@And("^I provide the country \"([^\"]*)\"$")
	public void iProvideTheCountry(String country)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlSelect select = (HtmlSelect) this.signupPage.getElementById("country");
		HtmlOption option = select.getOptionByValue(country);
		select.setSelectedAttribute(option, true);
	}

	@And("^I provide the password  \"([^\"]*)\"$")
	public void iProvideThePassword(String password)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("password").setValueAttribute(password);

	}

	@And("^I provide the password confirmation \"([^\"]*)\"$")
	public void iProvideThePasswordConfirmation(String confirmPassword)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.signupForm.getInputByName("confirmPassword")
				.setValueAttribute(confirmPassword);
	}

	@Then("^I should register as a customer$")
	public void iShouldRegisterAsCustomer()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlSubmitInput submit = this.signupPage
				.getFirstByXPath("//input[@type='submit']");
		assertThat(submit.click().getUrl().getPath(), equalTo("/login"));
	}
}