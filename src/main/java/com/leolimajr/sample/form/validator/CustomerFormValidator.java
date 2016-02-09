package com.leolimajr.sample.form.validator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.leolimajr.sample.form.CustomerForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerForm user = (CustomerForm) target;
		validateEmail(user, errors);
		validatePassword(user, errors);
		validateAge(user, errors);

	}

	protected void validateEmail(CustomerForm user, Errors errors) {
		if (user.getUsername() == null) {
			return;
		}
		// RFC-822 validation
		InternetAddress emailAddr;
		try {
			emailAddr = new InternetAddress(user.getUsername());
			emailAddr.validate();
		}
		catch (AddressException e) {
			errors.rejectValue("username", "email.invalid");
		}
	}

	protected void validatePassword(CustomerForm user, Errors errors) {
		if (user.getPassword() != null && !user.getPassword().trim().isEmpty()
				&& !user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "password.mismatch");
		}
	}

	protected void validateAge(CustomerForm user, Errors errors) {
		if (user.getBirthDate() == null) {
			return;
		}
		LocalDate today = LocalDate.now();
		int years = Period.between(
				Instant.ofEpochMilli(user.getBirthDate().getTime())
						.atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate(),
				today).getYears();
		if (years < 16 || years > 120) {
			errors.rejectValue("birthDate", "birthdate.invalid");
		}

	}
}