package com.leolimajr.sample.controller;

import javax.validation.Valid;

import com.leolimajr.sample.domain.enums.Country;
import com.leolimajr.sample.form.CustomerForm;
import com.leolimajr.sample.form.validator.CustomerFormValidator;
import com.leolimajr.sample.service.SignupService;
import com.leolimajr.sample.service.exception.UsernameAlreadyExistsException;
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
public class SignupController extends WebMvcConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SignupController.class);

	private CustomerFormValidator validator;
	private SignupService service;

	@Autowired
	public SignupController(CustomerFormValidator validator, SignupService service) {
		this.validator = validator;
		this.service = service;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(this.validator);
	}

	@ModelAttribute("countries")
	protected Country[] getCountryValues() {
		return Country.values();
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("customer", new CustomerForm());
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String handleUserCreateForm(
			@Valid @ModelAttribute("customer") CustomerForm customer,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "signup";
		}
		try {
			this.service.save(customer);
		}
		catch (UsernameAlreadyExistsException e) {
			LOGGER.info("Username already exists", e);
			bindingResult.rejectValue("username", "username.exists");
			return "signup";
		}
		return "redirect:/";
	}
}
