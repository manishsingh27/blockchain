package com.mks.blockchain.domain;

import java.security.PublicKey;

import com.mks.blockchain.helper.CryptoUtil;

public class TransactionOutput {
	
	public String id;
	public PublicKey reciepient; 
	public float value; 
	public String parentTransactionId; 
	
	
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = CryptoUtil.applySha256(CryptoUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
	}
	
	
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}

}
