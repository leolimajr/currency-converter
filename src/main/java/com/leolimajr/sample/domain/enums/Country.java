package com.leolimajr.sample.domain.enums;
/**
 * @author Leo Lima
 */
public enum Country {

	AU("Austria"), 
	BE("Belgium"), 
	BG("Bulgaria"), 
	CH("Switzerland"),
	CZ("Czech Republic"), 
	DE("Germany"), 
	DK("Denmark"), 
	E1("Balearic Islands"), 
	E2("Spain"), 
	EE("Estonia"), 
	FI("Finland"), 
	FR("France"), 
	G2("Northern Ireland"), 
	GB("Great Britain"),
	GR("Greece"), 
	HR("Croatia"), 
	HU("Hungary"), 
	IE("Ireland "), 
	IT("Italy"), 
	LT("Lithuania"), 
	LU("Luxembourg"), 
	LV("Latvia"), 
	NL("Netherlands"), 
	PL("Poland"), 
	PT("Portugal"), 
	SE("Sweden"), 
	SL("Slovenia");

	private final String name;

	Country(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
} 
