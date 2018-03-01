package com.mks.blockchain.domain;

import java.io.Serializable;

public class Transaction implements Serializable {

	
	private static final long serialVersionUID = 8057565494604828871L;

	private String sender;

    private String recipient;

    private long amount;

    public Transaction() {}

    public Transaction(String sender, String recipient, long amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
