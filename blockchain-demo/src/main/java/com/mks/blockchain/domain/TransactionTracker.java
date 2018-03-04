package com.mks.blockchain.domain;

import java.util.HashMap;

public class TransactionTracker {
	
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();  
	public static float minimumTransaction = 0.1f;


}
