package com.LLBank.components;

import java.time.LocalDate;

// 1.3.2 Creation of the Flow class
public abstract class Flow {

	private Long identifier;
	private String comment;
	private double amount;
	private Long targetAccountNumber;
	private boolean effect;
	private LocalDate dateOfFlow;

	private static Long counter = 0L;

	protected Flow(String comment, double amount, Long targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
		this.identifier = ++counter;
		this.comment = comment;
		this.amount = amount;
		this.targetAccountNumber = targetAccountNumber;
		this.effect = effect;
		this.dateOfFlow = dateOfFlow;
	}

	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Long getTargetAccountNumber() {
		return targetAccountNumber;
	}

	public void setTargetAccountNumber(Long targetAccountNumber) {
		this.targetAccountNumber = targetAccountNumber;
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}

	public LocalDate getDateOfFlow() {
		return dateOfFlow;
	}

	public void setDateOfFlow(LocalDate dateOfFlow) {
		this.dateOfFlow = dateOfFlow;
	}

}
