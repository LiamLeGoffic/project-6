package com.LLBank.components;

// 1.1.1 Creation of the client class
public class Client {

	private Long clientNumber;
	private String name;
	private String firstName;

	private static Long counter = 0L;

	public Client(String name, String firstName) {
		this.clientNumber = ++counter;
		this.name = name;
		this.firstName = firstName;
	}

	public Long getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(Long clientNumber) {
		this.clientNumber = clientNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "Client [clientNumber=" + clientNumber + ", name=" + name + ", firstName=" + firstName + "]";
	}

}
