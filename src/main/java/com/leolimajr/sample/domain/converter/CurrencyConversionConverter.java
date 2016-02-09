package com.leolimajr.sample.domain.converter;

import com.leolimajr.sample.domain.CurrencyConversion;
import com.leolimajr.sample.form.CurrencyConverterForm;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.stereotype.Component;

/**
 * @author Leo Lima
 */

@Component
public class CurrencyConversionConverter {

	private final ModelMapper modelMapper;

	public CurrencyConversionConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
	}

	public CurrencyConversion from(CurrencyConverterForm form) {
		return this.modelMapper.map(form, CurrencyConversion.class);
	}

	public CurrencyConverterForm to(CurrencyConversion conversion) {
		return this.modelMapper.map(conversion, CurrencyConverterForm.class);
	}
}