package com.leolimajr.sample.cucumber;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class CurrencyConversionStepsDefs extends BaseAcceptanceTestConfig {

	protected HtmlPage conversionPage;
	protected HtmlForm conversionForm;

	@Before
	public void setup() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("schema.sql").addScript("dml-login.sql").build();
	}

	@Given("^I am logged in with username \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void iAmLoggedIn(String username, String password)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.conversionPage = this.webClient
				.getPage("http://localhost:" + this.port + "/login");
		assertNotNull(this.conversionPage.getFirstByXPath("//form[@action='/login']"));
		this.conversionPage.getFormByName("form").getInputByName("username")
				.setValueAttribute(username);
		this.conversionPage.getFormByName("form").getInputByName("password")
				.setValueAttribute(password);
		HtmlSubmitInput submit = this.conversionPage
				.getFirstByXPath("//input[@type='submit']");
		this.conversionPage = submit.click();
	}

	@Given("^I am on the conversion page$")
	public void iAmOnTheConversionPage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		assertThat(this.conversionPage.getUrl().getPath(),
				equalTo("/currency/converter"));
		this.conversionForm = this.conversionPage.getFormByName("form");
	}

	@When("^I provide the currency I have \"([^\"]*)\"$")
	public void iProvideTheCurrencyIHave(String from)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlSelect select = (HtmlSelect) this.conversionPage.getElementById("from");
		HtmlOption option = select.getOptionByValue(from);
		select.setSelectedAttribute(option, true);
	}

	@And("^I provide the currency I want \"([^\"]*)\"$")
	public void iProvideTheCurrencyIWant(String to)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlSelect select = (HtmlSelect) this.conversionPage.getElementById("to");
		HtmlOption option = select.getOptionByValue(to);
		select.setSelectedAttribute(option, true);
	}

	@And("^I provide the amount I have \"([^\"]*)\"$")
	public void iProvideTheAmountIHave(String amount)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		this.conversionForm.getInputByName("amount").setValueAttribute(amount);
	}

	@Then("^I should get a currency conversion$")
	public void iShouldGetCurrencyConversion()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlSubmitInput submit = this.conversionPage
				.getFirstByXPath("//input[@type='submit']");
		this.conversionPage = submit.click();
		assertThat(this.conversionPage.getUrl().getPath(),
				equalTo("/currency/converter"));
		HtmlElement th = this.conversionPage.getFirstByXPath("//th[@scope='row']");
		assertThat(th, notNullValue());
		assertThat(th.getParentNode().getChildNodes().size(), equalTo(6));
		assertThat(th.getParentNode().getByXPath("//td[text()='USD']").size(),
				equalTo(1));
		assertThat(th.getParentNode().getByXPath("//td[text()='BRL']").size(),
				equalTo(1));
		assertThat(th.getParentNode().getChildNodes().get(3).getTextContent().isEmpty(),
				is(false));
		assertThat(th.getParentNode().getChildNodes().get(4).getTextContent().isEmpty(),
				is(false));
		assertThat(th.getParentNode().getChildNodes().get(5).getTextContent(),
				equalTo("-"));
	}
}