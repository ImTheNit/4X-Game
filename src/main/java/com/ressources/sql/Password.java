package com.ressources.sql;

public class Password {
	String password;
	byte[] salt;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	Password(String password, byte[] salt){
		setPassword(password);
		setSalt(salt);
	}
}
