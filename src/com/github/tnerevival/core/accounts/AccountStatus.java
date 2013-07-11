package com.github.tnerevival.core.accounts;

public enum AccountStatus {
	B("Bankrupt", "This account status is unused at the moment.", false),
	F("Frozen", "I'm sorry, but your account appears to be frozen.", false),
	N("Normal", "This message will never be used.", true);
	
	String name;
	String message;
	Boolean allowed;
	
	AccountStatus(String name, String message, Boolean allowed) {
		this.name = name;
		this.message = message;
		this.allowed = allowed;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the allowed
	 */
	public Boolean getAllowed() {
		return allowed;
	}
}