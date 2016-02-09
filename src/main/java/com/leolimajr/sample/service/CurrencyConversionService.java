package com.leolimajr.sample.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.leolimajr.sample.domain.CurrencyConversion;
import com.leolimajr.sample.domain.Customer;
import com.leolimajr.sample.domain.converter.CurrencyConversionConverter;
import com.leolimajr.sample.form.CurrencyConverterForm;
import com.leolimajr.sample.repository.CurrencyConversionRepository;
import com.leolimajr.sample.repository.specification.CurrencyConversionSpecifications;
import com.leolimajr.sample.service.exception.CannotGetExchangeRateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

/**
 * @author Leo Lima
 */
@Service
public class CurrencyConversionService {

	protected CurrencyConverterService converterService;
	protected CurrencyConversionRepository respository;
	protected CurrencyConversionConverter converter;

	@Autowired
	public CurrencyConversionService(CurrencyConverterService converterService,
			CurrencyConversionRepository respository,
			CurrencyConversionConverter converter) {
		this.converterService = converterService;
		this.respository = respository;
		this.converter = converter;
	}

	public CurrencyConverterForm convertAndSave(CurrencyConverterForm form,
			Customer customer) throws CannotGetExchangeRateException {

		Objects.requireNonNull(form);
		Objects.requireNonNull(form.getFrom());
		Objects.requireNonNull(form.getTo());
		Objects.requireNonNull(form.getAmount());
		Objects.requireNonNull(customer);
		Objects.requireNonNull(customer.getUsername());

		CurrencyConversion conversion = this.converter.from(form);
		conversion.setCustomer(customer);
		BigDecimal rate = null;
		if (form.getReference() == null) {
			rate = this.converterService.convert(conversion.getFrom(), conversion.getTo(),
					conversion.getAmount());
		}
		else {
			rate = this.converterService.convert(conversion.getFrom(), conversion.getTo(),
					conversion.getAmount(), conversion.getReference());
		}
		conversion.setRate(rate);
		this.respository.save(conversion);
		return this.converter.to(conversion);
	}

	public List<CurrencyConverterForm> findAllByCustomerOrderByIdDesc(Customer customer) {
		Objects.requireNonNull(customer);
		Objects.requireNonNull(customer.getUsername());

		List<CurrencyConversion> conversionList = this.respository
				.findAll(CurrencyConversionSpecifications.findByCustomer(customer),
						new PageRequest(0, 10, new Sort(new Order(Direction.DESC, "id"))))
				.getContent();

		return conversionList.parallelStream()
				.map(new Function<CurrencyConversion, CurrencyConverterForm>() {
					@Override
					public CurrencyConverterForm apply(
							CurrencyConversion currencyConversion) {
						return CurrencyConversionService.this.converter
								.to(currencyConversion);
					}

				}).collect(Collectors.toList());
	}
}