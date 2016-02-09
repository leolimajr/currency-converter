package com.leolimajr.sample.controller;

import java.security.Principal;

import javax.validation.Valid;

import com.leolimajr.sample.domain.Customer;
import com.leolimajr.sample.domain.enums.Currency;
import com.leolimajr.sample.form.CurrencyConverterForm;
import com.leolimajr.sample.form.validator.CurrencyConverterFormValidator;
import com.leolimajr.sample.service.CurrencyConversionService;
import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Leo Lima
 */

@Controller
public class CurrencyConverterController extends WebMvcConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CurrencyConverterController.class);

	private CurrencyConversionService service;
	private CurrencyConverterFormValidator validator;

	@Autowired
	public CurrencyConverterController(CurrencyConversionService service,
			CurrencyConverterFormValidator validator) {
		this.service = service;
		this.validator = validator;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(this.validator);
	}

	@ModelAttribute("currencies")
	protected Currency[] getCurrencyValues() {
		return Currency.values();
	}

	protected void loadHistory(Model model, Principal user) {
		model.addAttribute("history", this.service
				.findAllByCustomerOrderByIdDesc(new Customer(user.getName())));
	}

	@RequestMapping(value = "/currency/converter", method = RequestMethod.GET)
	public String home(Model model, Principal user) {

		model.addAttribute("converter", new CurrencyConverterForm());
		loadHistory(model, user);
		return "convert";
	}

	@RequestMapping(value = "/currency/converter", method = RequestMethod.POST)
	public String convert(
			@Valid @ModelAttribute("converter") CurrencyConverterForm converter,
			BindingResult bindingResult, Model model, Principal user) {

		if (!bindingResult.hasErrors()) {
			try {
				model.addAttribute("converter", this.service.convertAndSave(converter,
						new Customer(user.getName())));
			}
			catch (CannotGetExchangeRateException e) {
				LOGGER.error("Something wrong getting rates", e);
				bindingResult.reject("exchange.error");
			}
		}

		loadHistory(model, user);
		return "convert";
	}
}