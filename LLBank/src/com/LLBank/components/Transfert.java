package com.LLBank.components;

import java.time.LocalDate;

// 1.3.3 Creation of the Transfert, Credit, Debit classes
public class Transfert extends Flow {

	private Long issuierAccountNumber;

	public Transfert(String comment, double amount, Long targetAccountNumber, boolean effect, LocalDate dateOfFlow,
			Long issuierAccountNumber) {
		super(comment, amount, targetAccountNumber, effect, dateOfFlow);
		this.issuierAccountNumber = issuierAccountNumber;
	}

	public Long getIssuierAccountNumber() {
		return issuierAccountNumber;
	}

	public void setIssuierAccountNumber(Long issuierAccountNumber) {
		this.issuierAccountNumber = issuierAccountNumber;
	}

}
