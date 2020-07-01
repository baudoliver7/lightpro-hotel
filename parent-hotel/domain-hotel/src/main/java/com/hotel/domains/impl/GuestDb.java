package com.hotel.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.hotel.domains.api.Bookings;
import com.hotel.domains.api.Guest;
import com.hotel.domains.api.GuestMetadata;
import com.hotel.domains.api.Hotel;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Company;
import com.securities.api.Contact;
import com.securities.api.ContactNature;

public final class GuestDb extends GuidKeyEntityDb<Guest, GuestMetadata> implements Guest {

	private final transient Contact origin;
	private final Hotel module;
	
	public GuestDb(final Base base, final UUID id, final Hotel module){
		super(base, id, "H�te introuvable !");	
		this.module = module;
		this.origin = contact(id);		
	}
	
	private Contact contact(UUID id){
		try {
			return hotel().contacts().get(id);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String photo() throws IOException {
		return origin.photo();
	}

	@Override
	public Company company() throws IOException {
		return this.origin.company();
	}

	@Override
	public String fax() throws IOException {
		return origin.fax();
	}

	@Override
	public String locationAddress() throws IOException {
		return origin.locationAddress();
	}

	@Override
	public String mail() throws IOException {
		return origin.mail();
	}

	@Override
	public String mobile() throws IOException {
		return origin.mobile();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public ContactNature nature() throws IOException {
		return origin.nature();
	}

	@Override
	public String phone() throws IOException {
		return origin.phone();
	}

	@Override
	public String poBox() throws IOException {
		return origin.poBox();
	}

	@Override
	public void updateAddresses(String locationAddress, String phone, String mobile, String fax, String mail, String poBox, String webSite) throws IOException {
		origin.updateAddresses(locationAddress, phone, mobile, fax, mail, poBox, webSite);
	}

	@Override
	public String webSite() throws IOException {
		return origin.webSite();
	}

	@Override
	public Bookings bookings() throws IOException {
		return hotel().bookings().ofGuest(this);
	}

	@Override
	public Hotel hotel() throws IOException {
		return module;
	}
}
