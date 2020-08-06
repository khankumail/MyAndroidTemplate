package com.app.sample.mailbox.model;

import java.io.Serializable;

public class People implements Serializable {
	private long id;
	private String name;
	private String address;
	private int photo;

	public People(long id, String name, String address, int photo) {
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		if(name==null){
			return address;
		}
		return name;
	}

	public int getPhoto() {
		return photo;
	}
}
