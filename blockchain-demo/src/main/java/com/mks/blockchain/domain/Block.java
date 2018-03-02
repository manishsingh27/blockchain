package com.mks.blockchain.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mks.blockchain.helper.CryptoUtil;
import com.mks.blockchain.helper.Utilities;

public class Block implements Serializable {

	private static final long serialVersionUID = -1875593299885398418L;

	private long blockNumber;
	public String currentBlockHash;
	public String previousBlockHash;
	private long timeStamp;
	private int nonce;
	private List<Transaction> transactions;

	public Block() {
	}

	public Block(String previousHash, List<Transaction> transactions) {
		this.transactions = transactions;
		this.previousBlockHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.currentBlockHash = calculateHash();
	}

	public Block(List<Transaction> transactions) {
		this.transactions = transactions;
		this.timeStamp = new Date().getTime();
		this.currentBlockHash = calculateHash();
	}

	public String calculateHash() {
		String calculatedhash = CryptoUtil.applySha256(this.blockNumber + this.previousBlockHash
				+ Long.toString(this.timeStamp) + Integer.toString(this.nonce) + this.transactions.toString());
		return calculatedhash;
	}

	public void mineCurrentBlock(int noOfZero) {

		String startHashWithValue = Utilities.getZerosBasedOnInput(noOfZero);

		// hashing algorithm is re-run until its figure out which Nonce to be
		// set to get the noOfZero zero.
		
		while (currentBlockHash!= null && 
				!currentBlockHash.substring(0, noOfZero).equals(startHashWithValue)) {
			nonce++;
			currentBlockHash = calculateHash();
		}
		System.out.println("Block has been mined, nonce is  : " + nonce);
	}

	public long getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getcurrentBlockHash() {
		return currentBlockHash;
	}

	public void setCurrentBlockHash(String hash) {
		this.currentBlockHash = hash;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}

	public void setPreviousBlockHash(String previousHash) {
		this.previousBlockHash = previousHash;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
