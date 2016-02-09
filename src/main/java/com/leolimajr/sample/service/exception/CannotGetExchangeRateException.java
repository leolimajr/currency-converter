package com.leolimajr.sample.service.exception;

/**
 * @author Leo Lima
 */
public class CannotGetExchangeRateException extends Exception {

	private static final long serialVersionUID = 1L;

	CannotGetExchangeRateException() {
		super();
	}

	public CannotGetExchangeRateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CannotGetExchangeRateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotGetExchangeRateException(String message) {
		super(message);
	}

	public CannotGetExchangeRateException(Throwable cause) {
		super(cause);
	}
}