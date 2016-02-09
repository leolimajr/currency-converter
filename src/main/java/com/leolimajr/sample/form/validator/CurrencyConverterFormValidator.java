package com.leolimajr.sample.form.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import com.leolimajr.sample.form.CurrencyConverterForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CurrencyConverterFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CurrencyConverterForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CurrencyConverterForm form = (CurrencyConverterForm) target;
		validateAmount(form, errors);
		validateReference(form, errors);

	}

	private void validateReference(CurrencyConverterForm form, Errors errors) {
		if (form.getReference() == null) {
			return;
		}

		LocalDate today = LocalDate.now();
		LocalDate initialDate = LocalDate.of(1999, 1, 1);
		LocalDate reference = form.getReference().toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDate();

		if (today.until(reference, ChronoUnit.DAYS) > -1
				|| reference.isBefore(initialDate)) {
			errors.rejectValue("reference", "reference.invalid");
		}
	}

	protected void validateAmount(CurrencyConverterForm form, Errors errors) {
		if (form.getAmount() == null) {
			return;
		}
		if (form.getAmount().signum() <= 0) {
			errors.rejectValue("amount", "amount.invalid");
		}
	}
}