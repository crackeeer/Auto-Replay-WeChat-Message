package com.xrobot.di.bean;

import java.util.Date;

public class ChatMessage {

	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	private String msg;
	private Type type;//类型（发送or接收）
	private Date date;
	
	public enum Type
	{
		INCOMING, OUTCOMING
	}
	
}
