package com.LLBank.components;

import java.time.LocalDate;

// 1.3.3 Creation of the Transfert, Credit, Debit classes
public class Debit extends Flow {

	public Debit(String comment, double amount, Long targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
		super(comment, amount, targetAccountNumber, effect, dateOfFlow);
	}

}
