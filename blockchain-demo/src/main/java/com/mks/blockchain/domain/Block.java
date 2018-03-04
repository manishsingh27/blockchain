package com.mks.blockchain.domain;

import java.io.Serializable;
import java.util.ArrayList;
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
	public String merkleRoot;
	
	public List<CryptoTransaction> transactions = new ArrayList<CryptoTransaction>();

	public Block() {
	}
	
	public Block(String previousHash ) {
		this.previousBlockHash = previousHash;
		this.timeStamp = new Date().getTime();
		
		this.currentBlockHash = calculateHash(); //Making sure we do this after we set the other values.
	}

	public Block(String previousHash, List<CryptoTransaction> transactions) {
		this.transactions = transactions;
		this.previousBlockHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.currentBlockHash = calculateHash();
	}

	public Block(List<CryptoTransaction> transactions) {
		this.transactions = transactions;
		this.timeStamp = new Date().getTime();
		this.currentBlockHash = calculateHash();
	}

	public String calculateHash() {
		String calculatedhash = CryptoUtil.applySha256(this.blockNumber + this.previousBlockHash
				+ Long.toString(this.timeStamp) + Integer.toString(this.nonce) + merkleRoot);
		return calculatedhash;
	}

	public void mineCurrentBlock(int noOfZero) {

		merkleRoot = CryptoUtil.getMerkleRoot(transactions);

		String startHashWithValue = Utilities.getZerosBasedOnInput(noOfZero);

		// hashing algorithm is re-run until its figure out which Nonce to be
		// set to get the noOfZero zero.

		while (currentBlockHash != null && !currentBlockHash.substring(0, noOfZero).equals(startHashWithValue)) {
			nonce++;
			currentBlockHash = calculateHash();
		}
		System.out.println("Block has been mined, nonce is  : " + nonce);
	}

	// Add transactions to this block
	public boolean addTransaction(CryptoTransaction transaction) {

		if (transaction == null)
			return false;

		if ((previousBlockHash != "0")) {
			if ((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}

		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
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

	public List<CryptoTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<CryptoTransaction> transactions) {
		this.transactions = transactions;
	}

}
