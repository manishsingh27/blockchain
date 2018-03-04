package com.mks.blockchain.domain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import com.mks.blockchain.helper.CryptoUtil;

public class CryptoTransaction {

	private PublicKey sender;
	private PublicKey reciepient;
	private float value;
	private byte[] signature;
	private String transactionId;

	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	private static int sequence = 0;

	public CryptoTransaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}



	public String getTransactionId() {
		return transactionId;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PublicKey getSender() {
		return sender;
	}

	public void setSender(PublicKey sender) {
		this.sender = sender;
	}

	public PublicKey getReciepient() {
		return reciepient;
	}

	public void setReciepient(PublicKey reciepient) {
		this.reciepient = reciepient;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	private String calulateHash() {
		sequence++;
		return CryptoUtil.applySha256(CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(reciepient)
				+ Float.toString(value) + sequence);
	}

	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(reciepient)
				+ Float.toString(value);
		signature = CryptoUtil.applyECDSASig(privateKey, data);
	}

	public boolean verifiySignature() {
		String data = CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(reciepient)
				+ Float.toString(value);
		return CryptoUtil.verifyECDSASig(sender, data, signature);
	}

	public boolean processTransaction() {

		if (verifiySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		// Gathers transaction inputs (Making sure they are unspent):
		for (TransactionInput i : inputs) {
			i.UTXO = TransactionTracker.UTXOs.get(i.transactionOutputId);
		}

		// Checks if transaction is valid:
		if (getInputsValue() < TransactionTracker.minimumTransaction) {
			System.out.println("Transaction Inputs to small: " + getInputsValue());
			return false;
		}

		// Generate transaction outputs:
		float leftOver = getInputsValue() - value;
		transactionId = calulateHash();
		outputs.add(new TransactionOutput(this.reciepient, value, transactionId));

		outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

		// Add outputs to Unspent list
		for (TransactionOutput o : outputs) {
			TransactionTracker.UTXOs.put(o.id, o);
		}

		// Remove transaction inputs from UTXO lists as spent:
		for (TransactionInput i : inputs) {
			if (i.UTXO == null) {
				continue;
			}
			TransactionTracker.UTXOs.remove(i.UTXO.id);
		}

		return true;
	}

	public float getInputsValue() {
		float total = 0;
		for (TransactionInput i : inputs) {
			if (i.UTXO == null) {
				continue;
			}

			total += i.UTXO.value;
		}
		return total;
	}

	public float getOutputsValue() {
		float total = 0;
		for (TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}

}
