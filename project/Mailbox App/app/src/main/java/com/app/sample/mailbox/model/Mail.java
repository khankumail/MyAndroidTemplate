package com.app.sample.mailbox.model;

import java.io.Serializable;

public class Mail implements Serializable{
	private long id;
	private String date;
	private People sender;
	private People receiver;
	private String subject;
	private String content;
	private boolean from_me = false;

	public Mail(long id, String date, People sender, People receiver, String subject, String content) {
		this.id = id;
		this.date = date;
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public People getSender() {
		return sender;
	}

	public People getReceiver() {
		return receiver;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public boolean isFrom_me() {
		return from_me;
	}

	public void setFrom_me(boolean from_me) {
		this.from_me = from_me;
	}
}