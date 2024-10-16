package com.LLBank.components;

import java.util.Objects;

// 1.2.1 Creation of the account class
public abstract class Account {

	protected Long accountNumber;
	protected String label;
	protected double balance;
	protected Client client;

	private static Long counter = 0L;

	protected Account(String label, Client client) {
		this.accountNumber = ++counter;
		this.label = label;
		this.client = client;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getBalance() {
		return balance;
	}

	// 1.3.5 Updating accounts
	public void setBalance(Flow flow) {
		if (flow instanceof Credit) {
			this.balance += flow.getAmount();
		} else if (flow instanceof Debit) {
			this.balance -= flow.getAmount();
		} else if (flow instanceof Transfert) {
			if (Objects.equals(this.accountNumber, flow.getTargetAccountNumber())) {
				this.balance += flow.getAmount();
			} else if (Objects.equals(this.accountNumber, ((Transfert) flow).getIssuierAccountNumber())) {
				this.balance -= flow.getAmount();
			}
		}
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", label=" + label + ", balance=" + balance + ", client="
				+ client + "]";
	}

}
